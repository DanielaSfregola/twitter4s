package com.danielasfregola.twitter4s.http.clients.streaming

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream._
import akka.stream.scaladsl.{Framing, Keep, Sink, Source}
import akka.util.{ByteString, Timeout}
import com.danielasfregola.twitter4s.entities.streaming.{CommonStreamingMessage, StreamingMessage}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.exceptions.TwitterException
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.streaming.ActorStreamingClient.OpenConnection
import com.danielasfregola.twitter4s.http.oauth.OAuth1Provider
import org.json4s.native.Serialization
import org.reactivestreams.Publisher

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class ActorStreamingClient(consumerToken: ConsumerToken,
                           accessToken: AccessToken,
                           request: HttpRequest,
                           handleStreamErrors: Boolean = true)(implicit val actorSystem: ActorSystem,
                                                               implicit val mat: Materializer,
                                                               implicit val ec: ExecutionContext)
    extends Actor
    with OAuthClient
    with StreamFailureHandler
    with HttpConnectionFlow {

  override lazy val oauthProvider: OAuth1Provider = new OAuth1Provider(consumerToken, Some(accessToken))
  override def withLogRequest: Boolean = true
  override def withLogRequestResponse: Boolean = true

  implicit val timeout = Timeout(5 seconds)

  // Because it's an actor, we can use a var to keep track of the most recent error and backoff duration so that
  // we know what the next delay should be
//  private var failureRetryMonitor: Int = 0

  override def receive = {
    case _: OpenConnection =>
      openConnection().map(
        _ => sender() ! publisher
      )
    case csm: CommonStreamingMessage =>
      handleMessage(csm) match {
        case StreamFailureAction.RetryImmediately =>
          killSwitch.shutdown()
          if (handleStreamErrors) {
            logger.debug(s"Attempting to reconnect without delay")
            openConnection()
          }
        case StreamFailureAction.RetryForTcpIpError =>
          killSwitch.shutdown()
          if (handleStreamErrors) {
            // use something for scheduling next start, definitely not Thread.sleep()
          }
      }
  }

  private val actorSource = Source.actorRef[HttpRequest](1, OverflowStrategy.fail)
  private val publisherSink = Sink.asPublisher[StreamingMessage](fanout = false)
  private val killSwitch: SharedKillSwitch = KillSwitches.shared(s"twitter4s-${UUID.randomUUID}")

  private lazy val (graphSourceActorRef, publisher): (ActorRef, Publisher[StreamingMessage]) =
    actorSource
      .viaMat(connectionFlow)(Keep.left)
      .recover {
        case streamTcpException: StreamTcpException =>
          logger.error("HTTP connection has failed: ", streamTcpException)
          throw streamTcpException
      }
      .flatMapConcat(processHttpResponse)
      .toMat(publisherSink)(Keep.both)
      .run()

  def openConnection(): Future[Unit] = {
    logger.info(s"Opening HTTPS connection to ${request.uri}")
    withOAuthHeader(None)(mat)(request)
      .map(
        requestWithAuth => graphSourceActorRef ! requestWithAuth
      )
  }

  private def processHttpResponse(httpResponse: HttpResponse) = {
    logger.info(httpResponse.toString())
    httpResponse match {
      case response if response.status.isSuccess =>
        processBody(response, killSwitch)
      case failureResponse =>
        val statusCode = failureResponse.status
        val msg = "Connection could not be established"
        parseFailedResponse(failureResponse).map(ex => logger.error(s"$msg: $ex"))
        Source.failed(TwitterException(statusCode, s"$msg. Check the logs for more details"))
    }
  }

  private def processBody(
      response: HttpResponse,
      killSwitch: SharedKillSwitch
  ): Source[StreamingMessage, Any] =
    response.entity.withoutSizeLimit.dataBytes
      .via(Framing.delimiter(ByteString("\r\n"), Int.MaxValue).async)
      .filter(_.nonEmpty)
      .via(killSwitch.flow)
      .map(data => unmarshalStream(data))
      .filter(_.isSuccess)
      .map(_.get)

  private def unmarshalStream[T <: StreamingMessage: Manifest](data: ByteString): Try[StreamingMessage] = {
    val json = data.utf8String
    Try(Serialization.read[StreamingMessage](json))
  }

  override def getRequest: HttpRequest = request
}
object ActorStreamingClient {
  def props(consumerToken: ConsumerToken,
            accessToken: AccessToken,
            request: HttpRequest)(implicit actorSystem: ActorSystem, mat: Materializer, ec: ExecutionContext) =
    Props(new ActorStreamingClient(consumerToken = consumerToken, accessToken = accessToken, request = request))

  case class OpenConnection()
}

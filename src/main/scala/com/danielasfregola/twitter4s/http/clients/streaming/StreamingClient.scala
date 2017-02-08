package com.danielasfregola.twitter4s.http.clients.streaming

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream._
import akka.stream.scaladsl.{Framing, Sink, Source}
import akka.util.ByteString
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.exceptions.TwitterException
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.oauth.OAuth2Provider
import org.json4s.native.Serialization

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

private[twitter4s] class StreamingClient(val consumerToken: ConsumerToken, val accessToken: AccessToken) extends OAuthClient {

  val withLogRequest = true
  val withLogRequestResponse = false

  def preProcessing(): Unit = ()

  lazy val oauthProvider = new OAuth2Provider(consumerToken, Some(accessToken))

  private[twitter4s] implicit class RichStreamingHttpRequest(val request: HttpRequest) {

    def processStream[T <: StreamingMessage : Manifest](f: PartialFunction[T, Unit]): Future[TwitterStream] = {
      implicit val system = ActorSystem(s"twitter4s-streaming-${UUID.randomUUID}")
      implicit val materializer = ActorMaterializer()
      implicit val ec = materializer.executionContext
      for {
        requestWithAuth <- withOAuthHeader(None)(materializer)(request)
        killSwitch <- processOrFailStreamRequest(requestWithAuth)(f)
      } yield new TwitterStream(consumerToken, accessToken)(killSwitch, requestWithAuth, system)
    }
  }

  private val maxConnectionTimeMillis = 1000

  // TODO - can we do better?
  private def processOrFailStreamRequest[T <: StreamingMessage: Manifest](request: HttpRequest)(f: PartialFunction[T, Unit])
                                                                         (implicit system: ActorSystem, materializer: Materializer): Future[SharedKillSwitch] = {
    implicit val ec = materializer.executionContext
    val killSwitch = KillSwitches.shared(s"twitter4s-${UUID.randomUUID}")
    val processing = processStreamRequest(request, killSwitch)(f)
    val switch = Future { Thread.sleep(maxConnectionTimeMillis); killSwitch }
    Future.firstCompletedOf(Seq(processing, switch))
  }

  protected def processStreamRequest[T <: StreamingMessage: Manifest](request: HttpRequest, killSwitch: SharedKillSwitch)
                                                                     (f: PartialFunction[T, Unit])
                                                                     (implicit system: ActorSystem, materializer: Materializer): Future[SharedKillSwitch] = {
    implicit val ec = materializer.executionContext
    implicit val rqt = request

    if (withLogRequest) logRequest
    Source.single(request)
      .via(connection)
      .flatMapConcat {
        case response if response.status.isSuccess =>
          Future(processBody(response, killSwitch)(f))
          Source.empty
        case failureResponse =>
          val statusCode = failureResponse.status
          val msg = "Stream could not be opened"
          parseFailedResponse(failureResponse).map(ex => log.error(s"$msg: $ex"))
          Source.failed(TwitterException(statusCode, s"$msg. Check the logs for more details"))
      }
      .runWith(Sink.ignore)
      .map(_ => killSwitch)
  }

  def processBody[T: Manifest](response: HttpResponse, killSwitch: SharedKillSwitch)
                                        (f: PartialFunction[T, Unit])
                                        (implicit request: HttpRequest, materializer: Materializer): Unit =
    response.entity.withoutSizeLimit.dataBytes
      .via(Framing.delimiter(ByteString("\r\n"), Int.MaxValue).async)
      .filter(_.nonEmpty)
      .via(killSwitch.flow)
      .map(data => unmarshalStream(data, f))
      .runWith(Sink.foreach(_ => ()))

  private def unmarshalStream[T <: StreamingMessage: Manifest](data: ByteString, f: PartialFunction[T, Unit])(
      implicit request: HttpRequest): Unit = {
    val json = data.utf8String
    Try(Serialization.read[StreamingMessage](json)) match {
      case Success(message) =>
        message match {
          case msg: T if f.isDefinedAt(msg) =>
            log.debug("Processing message of type {}: {}", msg.getClass.getSimpleName, msg)
            f(msg)
          case msg => log.debug("Ignoring message of type {}", msg.getClass.getSimpleName)
        }
      case Failure(ex) => log.error(s"While processing stream ${request.uri}", ex)
    }
  }

}

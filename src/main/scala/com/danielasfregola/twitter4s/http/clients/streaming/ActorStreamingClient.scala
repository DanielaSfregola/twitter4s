package com.danielasfregola.twitter4s.http.clients.streaming

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Status}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.settings.ClientConnectionSettings
import akka.pattern.ask
import akka.stream.scaladsl.{Flow, Framing, Keep, Sink, Source}
import akka.stream._
import akka.util.{ByteString, Timeout}
import com.danielasfregola.twitter4s.entities.streaming.{CommonStreamingMessage, StreamingMessage}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.exceptions.TwitterException
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.oauth.OAuth1Provider
import org.json4s.native.Serialization
import org.reactivestreams.Publisher
import com.danielasfregola.twitter4s.http.clients.streaming.ActorStreamingClient.{GetPublisher, OpenConnection}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class ActorStreamingClient(consumerToken: ConsumerToken,
                           accessToken: AccessToken,
                           request: HttpRequest,
                           handleStreamErrors: Boolean = true)(implicit val actorSystem: ActorSystem,
                                                               implicit val mat: Materializer,
                                                               implicit val ec: ExecutionContext)
    extends Actor
    with OAuthClient
    with StreamFailureHandler {

  override lazy val oauthProvider: OAuth1Provider = new OAuth1Provider(consumerToken, Some(accessToken))
  override def withLogRequest: Boolean = true
  override def withLogRequestResponse: Boolean = true

  implicit val timeout = Timeout(5 seconds)

  // Because it's an actor, we can use a var to keep track of the most recent error and backoff duration so that
  // we know what the next delay should be
//  private var failureRetryMonitor: Int = 0

  override def receive = {
    case gc: GetPublisher => sender() ! publisher
    case oc: OpenConnection =>
      val originalSender = sender()
      openConnection()
        .recover({
          case ex: Exception =>
            {
              logger.error("WTF", ex)
            }
        })
        .onComplete {
          case Success(pb)            => {
            logger.info("alL GOOD")
          }
          case Failure(ex: Exception) => {
            logger.error("WTF2", ex)
            originalSender ! Status.Failure(ex)
          }
        }
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

  private val actorSource = Source.actorRef[HttpRequest](5, OverflowStrategy.fail)
  private val publisherSink = Sink.asPublisher[StreamingMessage](fanout = false)
  private val killSwitch: SharedKillSwitch = KillSwitches.shared(s"twitter4s-${UUID.randomUUID}")

//  val settings = ClientConnectionSettings()
  private lazy val connectionFlow = Http()
    .outgoingConnectionHttps(request.uri.authority.host.toString, request.uri.effectivePort)
    .log("test")
    .addAttributes(
      Attributes.logLevels(onElement = Attributes.LogLevels.Info,
        onFailure = Attributes.LogLevels.Error,
        onFinish = Attributes.LogLevels.Info))

  private lazy val (graphSourceActorRef: ActorRef, publisher: Publisher[StreamingMessage]) =
    actorSource
      .log("afterSource")
      .addAttributes(
        Attributes.logLevels(onElement = Attributes.LogLevels.Info,
                             onFailure = Attributes.LogLevels.Error,
                             onFinish = Attributes.LogLevels.Info))
      .via(connectionFlow)
//    .via(req => Http().singleRequest(req))
      .log("afterConnection")
      .addAttributes(
        Attributes.logLevels(onElement = Attributes.LogLevels.Info,
          onFailure = Attributes.LogLevels.Error,
          onFinish = Attributes.LogLevels.Info))
      .flatMapConcat(processHttpResponse)
      .toMat(publisherSink)(Keep.both)
      .run()

  def openConnection(): Future[Unit] = {
    logger.info(s"Opening HTTP connection to ${request.uri}")
    Future.successful(graphSourceActorRef ! request)

//    withOAuthHeader(None)(mat)(request).map(
//      requestWithAuth => {
//        logger.info(requestWithAuth.toString())
//        publisher
//      }
//    )
//          .map(_ => {
//            logger.debug(s"Connection to ${request.uri} opened successfully")
//            publisher
//          })
//          .recover({
//            case ex: Throwable =>
//              logger.info(s"Failed to open connection to ${request.uri}")
//              throw ex
//          }))
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

  private def getConnectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = {
    val host = request.uri.authority.host.toString
    val port = request.uri.effectivePort
    if (request.uri.scheme == "https")
      Http()
        .outgoingConnectionHttps(host, port)
        .log("test")
        .addAttributes(
          Attributes.logLevels(onElement = Attributes.LogLevels.Info,
                               onFailure = Attributes.LogLevels.Error,
                               onFinish = Attributes.LogLevels.Info))
    else
      Http()
        .outgoingConnection(host, port)
        .log("test")
        .addAttributes(
          Attributes.logLevels(onElement = Attributes.LogLevels.Info,
                               onFailure = Attributes.LogLevels.Error,
                               onFinish = Attributes.LogLevels.Info))
  }
}
object ActorStreamingClient {
  def props(consumerToken: ConsumerToken,
            accessToken: AccessToken,
            request: HttpRequest)(implicit actorSystem: ActorSystem, mat: Materializer, ec: ExecutionContext) =
    Props(new ActorStreamingClient(consumerToken = consumerToken, accessToken = accessToken, request = request))

  case class OpenConnection()
  case class GetPublisher()
}

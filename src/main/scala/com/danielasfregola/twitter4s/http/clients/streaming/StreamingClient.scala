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
import com.danielasfregola.twitter4s.http.oauth.OAuth1Provider
import org.json4s.native.Serialization

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

private[twitter4s] class StreamingClient(val consumerToken: ConsumerToken, val accessToken: AccessToken)(
    implicit val system: ActorSystem)
    extends OAuthClient {

  val withLogRequest = true
  val withLogRequestResponse = false

  def preProcessing(): Unit = ()

  lazy val oauthProvider = new OAuth1Provider(consumerToken, Some(accessToken))

  private[twitter4s] implicit class RichStreamingHttpRequest(val request: HttpRequest) {

    implicit val materializer = ActorMaterializer()
    implicit val ec = materializer.executionContext

    def processStream[T <: StreamingMessage: Manifest](
        f: PartialFunction[T, Unit],
        errorHandler: PartialFunction[Throwable, Unit]
    ): Future[TwitterStream] =
      for {
        requestWithAuth <- withOAuthHeader(None)(materializer)(request)
        killSwitch <- processStreamRequest(requestWithAuth)(f, errorHandler)
      } yield TwitterStream(consumerToken, accessToken)(killSwitch, requestWithAuth, system)
  }

  protected def processStreamRequest[T <: StreamingMessage: Manifest](
      request: HttpRequest
  )(
      f: PartialFunction[T, Unit],
      errorHandler: PartialFunction[Throwable, Unit]
  )(
      implicit
      system: ActorSystem,
      materializer: Materializer
  ): Future[SharedKillSwitch] = {
    implicit val ec = materializer.executionContext
    implicit val rqt = request

    var successResponse = false

    if (withLogRequest) logRequest

    val killSwitch = KillSwitches.shared(s"twitter4s-${UUID.randomUUID}")

    val processing = Source
      .single(request)
      .via(connection)
      .flatMapConcat {
        case response if response.status.isSuccess =>
          successResponse = true
          processBody(response, killSwitch)(f, errorHandler)
          Source.empty
        case failureResponse =>
          val statusCode = failureResponse.status
          val msg = "Stream could not be opened"
          parseFailedResponse(failureResponse).map(ex => logger.error(s"$msg: $ex"))
          Source.failed(TwitterException(statusCode, s"$msg. Check the logs for more details"))
      }
      .runWith(Sink.ignore)
      .map(_ => killSwitch)

    val switch = Future {
      val pullingTimeMillis = 250
      while (!successResponse) { Thread.sleep(pullingTimeMillis) }
      killSwitch
    }

    Future.firstCompletedOf(Seq(processing, switch))
  }

  def processBody[T: Manifest, A](
      response: HttpResponse,
      killSwitch: SharedKillSwitch
  )(
      f: PartialFunction[T, Unit],
      errorHandler: PartialFunction[Throwable, Unit] = ErrorHandler.ignore
  )(
      implicit
      request: HttpRequest,
      materializer: Materializer
  ): Unit =
    response.entity.withoutSizeLimit.dataBytes
      .via(Framing.delimiter(ByteString("\r\n"), Int.MaxValue).async)
      .filter(_.nonEmpty)
      .via(killSwitch.flow)
      .map(data => unmarshalStream(data, f))
      .recoverWithRetries(attempts = 3, {
        case e =>
          errorHandler(e)
          Source.empty
      })
      .runWith(Sink.foreach(_ => (): Unit))

  private def unmarshalStream[T <: StreamingMessage: Manifest](data: ByteString, f: PartialFunction[T, Unit])(
      implicit request: HttpRequest): Unit = {
    val json = data.utf8String
    Try(Serialization.read[StreamingMessage](json)) match {
      case Success(message) =>
        message match {
          case msg: T if f.isDefinedAt(msg) =>
            logger.debug("Processing message of type {}: {}", msg.getClass.getSimpleName, msg)
            f(msg)
          case msg => logger.debug("Ignoring message of type {}", msg.getClass.getSimpleName)
        }
      case Failure(ex) => logger.error(s"While processing stream ${request.uri}", ex)
    }
  }

}

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

    import cats.effect._

    def processStream[T <: StreamingMessage : Manifest]
      (f: PartialFunction[T, Unit]): Future[TwitterStream] = {
      implicit val system = ActorSystem(s"twitter4s-streaming-${UUID.randomUUID}")
      implicit val materializer = ActorMaterializer()
      implicit val ec = materializer.executionContext
      for {
        requestWithAuth <- withOAuthHeader(None)(materializer)(request)
        killSwitch <- processOrFailStreamRequest(requestWithAuth)(f)
      } yield TwitterStream(consumerToken, accessToken)(killSwitch, requestWithAuth, system)
    }

    def processStreamFS2[T <: StreamingMessage : Manifest]
      (fs2Sink: fs2.Sink[IO, T]): Future[TwitterStream] = {
      implicit val system = ActorSystem(s"twitter4s-streaming-${UUID.randomUUID}")
      implicit val materializer = ActorMaterializer()
      implicit val ec = materializer.executionContext
      for {
        requestWithAuth <- withOAuthHeader(None)(materializer)(request)
        killSwitch <- FS2.processOrFailStreamRequest(requestWithAuth)(fs2Sink)
      } yield TwitterStream(consumerToken, accessToken)(killSwitch, requestWithAuth, system)
    }
  }

  private val maxConnectionTimeMillis = 1000

  // TODO - can we do better?
  private def processOrFailStreamRequest[T <: StreamingMessage: Manifest]
    (request: HttpRequest)
    (f: PartialFunction[T, Unit])
    (implicit system: ActorSystem, materializer: Materializer): Future[SharedKillSwitch] = {
    implicit val ec = materializer.executionContext
    val killSwitch = KillSwitches.shared(s"twitter4s-${UUID.randomUUID}")
    val processing = processStreamRequest(request, killSwitch)(f)
    val switch = Future { Thread.sleep(maxConnectionTimeMillis); killSwitch }
    Future.firstCompletedOf(Seq(processing, switch))
  }

  protected def processStreamRequest[T <: StreamingMessage: Manifest]
    (request: HttpRequest, killSwitch: SharedKillSwitch)
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
          parseFailedResponse(failureResponse).map(ex => logger.error(s"$msg: $ex"))
          Source.failed(TwitterException(statusCode, s"$msg. Check the logs for more details"))
      }
      .runWith(Sink.ignore)
      .map(_ => killSwitch)
  }

  def processBody[T: Manifest]
    (response: HttpResponse, killSwitch: SharedKillSwitch)
    (f: PartialFunction[T, Unit])
    (implicit request: HttpRequest, materializer: Materializer): Unit =
    response.entity.withoutSizeLimit.dataBytes
      .via(Framing.delimiter(ByteString("\r\n"), Int.MaxValue).async)
      .filter(_.nonEmpty)
      .via(killSwitch.flow)
      .map(data => unmarshalStream(data, f))
      .runWith(Sink.foreach(_ => (): Unit))

  protected object FS2 {
    // import fs2
    import cats.effect._
    // import streamz.converter
    // import streamz.converter._

    // val fs2PrintSink: fs2.Sink[IO, String] = fs2.Sink(s => IO(println("FS2: "+s)))

    // https://github.com/krasserm/streamz/blob/master/streamz-converter/README.md#conversions-from-akka-stream-to-fs2

    def processOrFailStreamRequest[T <: StreamingMessage: Manifest]
      (request: HttpRequest)
      (fs2Sink: fs2.Sink[IO, T])
      (implicit system: ActorSystem, materializer: Materializer): Future[SharedKillSwitch] = {
      implicit val ec = materializer.executionContext
      val killSwitch = KillSwitches.shared(s"twitter4s-${UUID.randomUUID}")
      val processing = FS2.processStreamRequest(request, killSwitch)(fs2Sink)
      val switch = Future { Thread.sleep(maxConnectionTimeMillis); killSwitch }
      Future.firstCompletedOf(Seq(processing, switch))
    }

    protected def processStreamRequest[T <: StreamingMessage: Manifest]
      (request: HttpRequest, killSwitch: SharedKillSwitch)
      (fs2Sink: fs2.Sink[IO, T])
      (implicit system: ActorSystem, materializer: Materializer): Future[SharedKillSwitch] = {
      implicit val ec = materializer.executionContext
      implicit val rqt = request

      if (withLogRequest) logRequest
      Source.single(request)
        .via(connection)
        .flatMapConcat {
          case response if response.status.isSuccess =>
            Future(FS2.processBody(response, killSwitch)(fs2Sink))
            Source.empty
          case failureResponse =>
            val statusCode = failureResponse.status
            val msg = "Stream could not be opened"
            parseFailedResponse(failureResponse).map(ex => logger.error(s"$msg: $ex"))
            Source.failed(TwitterException(statusCode, s"$msg. Check the logs for more details"))
        }
        .runWith(Sink.ignore)
        .map(_ => killSwitch)
    }

    def processBody[T: Manifest]
      (response: HttpResponse, killSwitch: SharedKillSwitch)
      (fs2Sink: fs2.Sink[IO, T])
      (implicit request: HttpRequest, materializer: Materializer): Unit =
      response.entity.withoutSizeLimit.dataBytes
        .via(Framing.delimiter(ByteString("\r\n"), Int.MaxValue).async)
        .filter(_.nonEmpty)
        .via(killSwitch.flow)
        .map(data => FS2.unmarshalStream(data, fs2Sink))
        .runWith(Sink.foreach(_ => (): Unit))

    private def unmarshalStream[T <: StreamingMessage: Manifest]
      (data: ByteString, fs2Sink: fs2.Sink[IO, T])
      (implicit request: HttpRequest, materializer: Materializer): Unit = {
      implicit val ec = materializer.executionContext
      
      val json = data.utf8String
      Try(Serialization.read[StreamingMessage](json)) match {
        case Success(message) =>
          message match {
            case msg: T =>
              logger.debug("Processing message of type {}: {}", msg.getClass.getSimpleName, msg)
              val eq: IO[Unit] = fs2.Stream.emit(msg).observe(fs2Sink).drain.run
              //eq.unsafeRunSync()
              eq.unsafeRunAsync {
                case Left(err) => println("failed to enqueue Tweet: "+err.toString())
                case Right(_) => ()
              }
          }
        case Failure(ex) => logger.error(s"While processing stream ${request.uri}", ex)
      }
    }
    
  }

  private def unmarshalStream[T <: StreamingMessage: Manifest]
    (data: ByteString, f: PartialFunction[T, Unit])
    (implicit request: HttpRequest): Unit = {
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

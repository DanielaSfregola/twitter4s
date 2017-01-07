package com.danielasfregola.twitter4s.http.clients

import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Sink, Source}
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import org.json4s.native.Serialization

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait StreamingClient extends OAuthClient {

  val withLogRequest = true
  val withLogRequestResponse = false

  private[twitter4s] implicit class RichStreamingHttpRequest(val request: HttpRequest) {

    def processStream[T <: StreamingMessage: Manifest](f: PartialFunction[T, Unit]): Future[Unit] =
      for {
        requestWithAuth <- withOAuthHeader(request)
        _ <- processStreamRequest(requestWithAuth)(f)
      } yield ()
  }

  protected def processStreamRequest[T <: StreamingMessage: Manifest](request: HttpRequest)(f: PartialFunction[T, Unit]): Future[Unit] = {
    implicit val _ = request
    val requestStartTime = System.currentTimeMillis

    if (withLogRequest) logRequest
    Source
    .single(request)
    .via(connection)
    .mapAsync(1) { implicit response =>
      val processor = { resp: HttpResponse => unmarshalStream(resp, f) }
      unmarshal[Unit](requestStartTime, processor)
    }
    .runWith(Sink.head)
  }

  protected def unmarshalStream[T <: StreamingMessage: Manifest](response: HttpResponse, f: PartialFunction[T, Unit])(implicit request: HttpRequest): Future[Unit] = {
    response.entity.withoutSizeLimit.dataBytes
    .scan("")((acc, curr) => if (acc.contains("\r\n")) curr.utf8String else acc + curr.utf8String)
    .filter(_.contains("\r\n"))
    .map { json =>
      Try(Serialization.read[StreamingMessage](json))
    }
    .runForeach {
      case Success(message) =>
        message match {
          case msg: T if f.isDefinedAt(msg) =>
            log.debug("Processing message of type {}: {}", msg.getClass.getSimpleName, msg)
            f(msg)
          case msg => log.debug("Ignoring message of type {}", msg.getClass.getSimpleName)
        }
      case Failure(ex) => log.error(ex, s"While processing stream ${request.uri}")
    }.map { _ => () }
  }

}

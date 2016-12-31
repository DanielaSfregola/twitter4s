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

    private val ignore: PartialFunction[StreamingMessage, Unit] = {
      case msg => log.debug(s"Ignoring message of type ${msg.getClass.getSimpleName}: $msg")
    }

    private def processOrIgnore[T <: StreamingMessage](f: PartialFunction[T, Unit]): StreamingMessage => Unit = { msg =>
      f orElse ignore
    }

    def processStream[T <: StreamingMessage](f: PartialFunction[T, Unit]): Future[Unit] =
      for {
        requestWithAuth <- withOAuthHeader(request)
        _ <- processStreamRequest(requestWithAuth)(processOrIgnore(f))
      } yield ()
  }

  protected def processStreamRequest(request: HttpRequest)(f: StreamingMessage => Unit): Future[Unit] = {
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

  protected def unmarshalStream(response: HttpResponse, f: StreamingMessage => Unit)(implicit request: HttpRequest): Future[Unit] = {
    response.entity.dataBytes
    .scan("")((acc, curr) => if (acc.contains("\r\n")) curr.utf8String else acc + curr.utf8String)
    .filter(_.contains("\r\n"))
    .map { json =>
      Try(Serialization.read[StreamingMessage](json))
    }
    .runForeach {
      case Success(msg) => f(msg)
      case Failure(ex) => log.error(ex, s"While processing stream ${request.uri}")
    }.map { _ => () }
  }
}

package com.danielasfregola.twitter4s.http.clients

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Future

trait Client extends CommonClient {
  val withLogRequest = false
  val withLogRequestResponse = false

  protected def sendAndReceive[T](request: HttpRequest, f: HttpResponse => Future[T])(
      implicit system: ActorSystem,
      materializer: Materializer): Future[T] = {
    implicit val r: HttpRequest = request
    val requestStartTime = System.currentTimeMillis

    if (withLogRequest) logRequest

    Source
      .single(request)
      .via(connection)
      .mapAsync(1)(implicit response => unmarshal(requestStartTime, f))
      .runWith(Sink.head)
  }
}

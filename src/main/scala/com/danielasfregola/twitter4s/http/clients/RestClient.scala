package com.danielasfregola.twitter4s.http.clients

import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Future

trait RestClient extends OAuthClient {

  val withLogRequest = false
  val withLogRequestResponse = true

  private[twitter4s] implicit class RichRestHttpRequest(val request: HttpRequest) {

    def respondAs[T: Manifest]: Future[T] =
      for {
        requestWithAuth <- withOAuthHeader(request)
        t <- sendReceiveAs[T](requestWithAuth)
      } yield t

    def sendAsFormData: Future[Unit] =
      for {
        requestWithAuth <- withSimpleOAuthHeader(request)
        _ <- sendReceiveAs[Any](requestWithAuth)
      } yield ()
  }

  def sendReceiveAs[T: Manifest](httpRequest: HttpRequest): Future[T] =
    sendAndReceive(httpRequest, response => json4sUnmarshaller[T].apply(response.entity))

  protected def sendAndReceive[T](request: HttpRequest, f: HttpResponse => Future[T]): Future[T] = {
    implicit val _ = request
    val requestStartTime = System.currentTimeMillis

    if (withLogRequest) logRequest

    Source
      .single(request)
      .via(connection)
      .mapAsync(1) { implicit response =>
        unmarshal(requestStartTime, f)
      }
      .runWith(Sink.head)
  }

}

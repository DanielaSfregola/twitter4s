package com.danielasfregola.twitter4s.http.clients.rest

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, Materializer}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken, RateLimit, RatedData}
import com.danielasfregola.twitter4s.http.clients.OAuthClient

import scala.concurrent.Future

private[twitter4s] class RestClient(val consumerToken: ConsumerToken, val accessToken: AccessToken) extends OAuthClient {

  val withLogRequest = false
  val withLogRequestResponse = true

  private[twitter4s] implicit class RichRestHttpRequest(val request: HttpRequest) {

    def respondAs[T: Manifest]: Future[T] = {
      implicit val system = ActorSystem(s"twitter4s-rest-${UUID.randomUUID}")
      implicit val materializer = ActorMaterializer()
      implicit val ec = materializer.executionContext
      for {
        requestWithAuth <- withOAuthHeader(materializer)(request)
        t <- sendReceiveAs[T](requestWithAuth)
        _ <- system.terminate
      } yield t
    }

    def respondAsRated[T: Manifest]: Future[RatedData[T]] = {
      implicit val system = ActorSystem(s"twitter4s-rest-${UUID.randomUUID}")
      implicit val materializer = ActorMaterializer()
      implicit val ec = materializer.executionContext
      for {
        requestWithAuth <- withOAuthHeader(materializer)(request)
        t <- sendReceiveAsRated[T](requestWithAuth)
        _ <- system.terminate
      } yield t
    }

    def sendAsFormData: Future[Unit] = {
      implicit val system = ActorSystem(s"twitter4s-rest-${UUID.randomUUID}")
      implicit val materializer = ActorMaterializer()
      implicit val ec = materializer.executionContext
      for {
        requestWithAuth <- withSimpleOAuthHeader(materializer)(request)
        _ <- sendReceiveAs[Any](requestWithAuth)
        _ <- system.terminate
      } yield ()
    }
  }

  def sendReceiveAs[T: Manifest](httpRequest: HttpRequest)
                                (implicit system: ActorSystem, materializer: Materializer): Future[T] = {
    implicit val ec = materializer.executionContext
    sendAndReceive(httpRequest, response => json4sUnmarshaller[T].apply(response.entity))
  }

  def sendReceiveAsRated[T: Manifest](httpRequest: HttpRequest)
                                (implicit system: ActorSystem, materializer: Materializer): Future[RatedData[T]] = {
    implicit val ec = materializer.executionContext
    val unmarshallRated: HttpResponse => Future[RatedData[T]] = { response =>
      val rate = RateLimit(response.headers)
      val data = json4sUnmarshaller[T].apply(response.entity)
      data.map(d => RatedData(rate, d))
    }
    sendAndReceive(httpRequest, unmarshallRated)
  }

  protected def sendAndReceive[T](request: HttpRequest, f: HttpResponse => Future[T])
                                 (implicit system: ActorSystem, materializer: Materializer): Future[T] = {
    implicit val _ = request
    val requestStartTime = System.currentTimeMillis

    if (withLogRequest) logRequest

    Source
      .single(request)
      .via(connection)
      .mapAsync(1)(implicit response => unmarshal(requestStartTime, f))
      .runWith(Sink.head)
  }

}

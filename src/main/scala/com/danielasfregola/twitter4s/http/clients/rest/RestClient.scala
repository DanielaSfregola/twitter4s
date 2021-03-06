package com.danielasfregola.twitter4s.http.clients.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, Materializer}
import com.danielasfregola.twitter4s.entities.{RateLimit, RatedData}
import com.danielasfregola.twitter4s.http.clients.Client
import com.danielasfregola.twitter4s.http.oauth.AuthClient

import scala.concurrent.Future

private[twitter4s] class RestClient(val authClient: AuthClient)(implicit val system: ActorSystem) extends Client {

  private[twitter4s] implicit class RichRestHttpRequest(val request: HttpRequest) {

    implicit val materializer = ActorMaterializer()
    implicit val ec = materializer.executionContext

    def respondAs[T: Manifest]: Future[T] =
      for {
        requestWithAuth <- authClient.withAuthHeader(materializer)(request)
        t <- sendReceiveAs[T](requestWithAuth)
      } yield t

    def respondAsRated[T: Manifest]: Future[RatedData[T]] =
      for {
        requestWithAuth <- authClient.withAuthHeader(materializer)(request)
        t <- sendReceiveAsRated[T](requestWithAuth)
      } yield t

    def sendAsFormData: Future[Unit] =
      for {
        requestWithAuth <- authClient.withSimpleAuthHeader(materializer)(request)
        _ <- sendIgnoreResponse(requestWithAuth)
      } yield ()
  }

  def sendIgnoreResponse(httpRequest: HttpRequest)(implicit system: ActorSystem,
                                                   materializer: Materializer): Future[Unit] = {
    sendAndReceive(httpRequest, _ => Future.successful((): Unit))
  }

  def sendReceiveAs[T: Manifest](httpRequest: HttpRequest)(implicit system: ActorSystem,
                                                           materializer: Materializer): Future[T] = {
    implicit val ec = materializer.executionContext
    implicit val jsonSerialization = serialization
    sendAndReceive(httpRequest, response => Unmarshal(response.entity).to[T])
  }

  def sendReceiveAsRated[T: Manifest](httpRequest: HttpRequest)(implicit system: ActorSystem,
                                                                materializer: Materializer): Future[RatedData[T]] = {
    implicit val ec = materializer.executionContext
    implicit val jsonSerialization = serialization
    val unmarshallRated: HttpResponse => Future[RatedData[T]] = { response =>
      val rate = RateLimit(response.headers)
      val data = Unmarshal(response.entity).to[T]
      data.map(d => RatedData(rate, d))
    }
    sendAndReceive(httpRequest, unmarshallRated)
  }
}

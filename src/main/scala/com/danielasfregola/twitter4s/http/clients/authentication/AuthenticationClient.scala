package com.danielasfregola.twitter4s.http.clients.authentication

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.stream.{ActorMaterializer, Materializer}
import com.danielasfregola.twitter4s.entities.ConsumerToken
import com.danielasfregola.twitter4s.http.clients.Client
import com.danielasfregola.twitter4s.http.oauth.OAuth1Provider
import com.danielasfregola.twitter4s.http.serializers.{FormSupport, FromMap}

import scala.concurrent.Future

private[twitter4s] class AuthenticationClient(val consumerToken: ConsumerToken)(implicit val system: ActorSystem)
    extends Client {

  lazy val oauthProvider = new OAuth1Provider(consumerToken, None)

  private[twitter4s] implicit class RichRestHttpRequest(val request: HttpRequest) {
    implicit val materializer = ActorMaterializer()
    implicit val ec = materializer.executionContext

    def respondAs[T: Manifest](implicit fromMap: FromMap[T]): Future[T] = respondAs[T](None)

    def respondAs[T: Manifest](callback: Option[String])(implicit fromMap: FromMap[T]): Future[T] =
      for {
        requestWithAuth <- withOAuthHeader(callback)(materializer)(request)
        t <- sendReceiveAs[T](requestWithAuth)
      } yield t
  }

  def sendReceiveAs[T: Manifest](httpRequest: HttpRequest)(implicit system: ActorSystem,
                                                           materializer: Materializer,
                                                           fromMap: FromMap[T]): Future[T] = {
    sendAndReceive(httpRequest, response => FormSupport.unmarshallText(response))
  }

}

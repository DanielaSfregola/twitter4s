package com.danielasfregola.twitter4s.http.clients.authentication

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.stream.{ActorMaterializer, Materializer}
import com.danielasfregola.twitter4s.entities.ConsumerToken
import com.danielasfregola.twitter4s.http.clients.Client
import com.danielasfregola.twitter4s.http.oauth.OAuth2Provider
import com.danielasfregola.twitter4s.http.serializers.{FormSupport, FromMap}

import scala.concurrent.Future

private[twitter4s] class AuthenticationClient(val consumerToken: ConsumerToken) extends Client {

  lazy val oauthProvider = new OAuth2Provider(consumerToken, None)

  private[twitter4s] implicit class RichRestHttpRequest(val request: HttpRequest) {

    def respondAs[T: Manifest](implicit fromMap: FromMap[T]): Future[T] = respondAs[T](None)

    def respondAs[T: Manifest](callback: Option[String])(implicit fromMap: FromMap[T]): Future[T] = {
      implicit val system = ActorSystem(s"twitter4s-authentication-${UUID.randomUUID}")
      implicit val materializer = ActorMaterializer()
      implicit val ec = materializer.executionContext
      val response = for {
        requestWithAuth <- withOAuthHeader(callback)(materializer)(request)
        t <- sendReceiveAs[T](requestWithAuth)
      } yield t
      response.onComplete(_ => system.terminate)
      response
    }
  }

  def sendReceiveAs[T: Manifest](httpRequest: HttpRequest)(implicit system: ActorSystem,
                                                           materializer: Materializer,
                                                           fromMap: FromMap[T]): Future[T] = {
    sendAndReceive(httpRequest, response => FormSupport.unmarshallText(response))
  }

}

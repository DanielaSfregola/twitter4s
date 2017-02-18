package com.danielasfregola.twitter4s.http.clients.authentication

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.stream.{ActorMaterializer, Materializer}
import com.danielasfregola.twitter4s.entities.ConsumerToken
import com.danielasfregola.twitter4s.http.clients.Client
import com.danielasfregola.twitter4s.http.oauth.OAuth2Provider
import com.danielasfregola.twitter4s.http.serializers.FormSupport
import shapeless.{HList, LabelledGeneric}

import scala.concurrent.Future

private[twitter4s] class AuthenticationClient(val consumerToken: ConsumerToken) extends Client {

  lazy val oauthProvider = new OAuth2Provider(consumerToken, None)

  private[twitter4s] implicit class RichRestHttpRequest(val request: HttpRequest) {

    def respondAs[T: Manifest, R <: HList](implicit gen: LabelledGeneric.Aux[T, R], fromMap: FormSupport.FromMap[R]): Future[T] = respondAs[T, R](None)

    def respondAs[T: Manifest, R <: HList](callback: Option[String])(implicit gen: LabelledGeneric.Aux[T, R], fromMap: FormSupport.FromMap[R]): Future[T] = {
      implicit val system = ActorSystem(s"twitter4s-authentication-${UUID.randomUUID}")
      implicit val materializer = ActorMaterializer()
      implicit val ec = materializer.executionContext
      for {
        requestWithAuth <- withOAuthHeader(callback)(materializer)(request)
        t <- sendReceiveAs[T, R](requestWithAuth)
        _ <- system.terminate
      } yield t
    }
  }

  def sendReceiveAs[T: Manifest, R <: HList](httpRequest: HttpRequest)(implicit system: ActorSystem,
                                                                       materializer: Materializer,
                                                                       gen: LabelledGeneric.Aux[T, R],
                                                                       fromMap: FormSupport.FromMap[R]): Future[T] = {
    implicit val ec = materializer.executionContext
    sendAndReceive(httpRequest, response => FormSupport.unmarshallText(response))
  }

}

package com.danielasfregola.twitter4s.http.oauth

import akka.http.scaladsl.model._
import akka.stream.Materializer
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

import scala.concurrent.Future

private[twitter4s] class OAuthClient(consumerToken: ConsumerToken, accessToken: AccessToken) extends AuthClient {

  lazy val oauthProvider = new OAuth1Provider(consumerToken, Some(accessToken))

  def withAuthHeader(callback: Option[String])(
      implicit materializer: Materializer): HttpRequest => Future[HttpRequest] = { request =>
    implicit val ec = materializer.executionContext
    for {
      authorizationHeader <- oauthProvider.oauth1Header(callback)(request, materializer)
    } yield request.withHeaders(request.headers :+ authorizationHeader)
  }

}

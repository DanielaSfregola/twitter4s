package com.danielasfregola.twitter4s.http.clients

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.Materializer
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.marshalling.{BodyEncoder, Parameters}
import com.danielasfregola.twitter4s.http.oauth.OAuth1Provider

import scala.concurrent.{ExecutionContext, Future}

private[twitter4s] class OAuthClient(consumerToken: ConsumerToken, accessToken: AccessToken) extends CommonClient {
  lazy val oauthProvider = new OAuth1Provider(consumerToken, Some(accessToken))
  override def withLogRequest: Boolean = false
  override def withLogRequestResponse: Boolean = false

  def withAuthHeader(callback: Option[String])(
      implicit materializer: Materializer): HttpRequest => Future[HttpRequest] = { request =>
    implicit val ec = materializer.executionContext
    for {
      authorizationHeader <- oauthProvider.oauth1Header(callback)(request, materializer)
    } yield request.withHeaders(request.headers :+ authorizationHeader)
  }
}

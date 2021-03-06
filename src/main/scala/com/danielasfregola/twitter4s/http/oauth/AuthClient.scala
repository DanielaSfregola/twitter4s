package com.danielasfregola.twitter4s.http.oauth

import akka.http.scaladsl.model._
import akka.stream.Materializer
import com.danielasfregola.twitter4s.entities.{AccessToken, BearerToken, ConsumerToken}
import com.danielasfregola.twitter4s.util.Configurations._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait AuthClient {

  def withLogRequest: Boolean = false
  def withLogRequestResponse: Boolean = false

  def withAuthHeader(implicit materializer: Materializer): HttpRequest => Future[HttpRequest] =
    withAuthHeader(callback = None)

  def withAuthHeader(callback: Option[String])(implicit materializer: Materializer): HttpRequest => Future[HttpRequest]

  def withSimpleAuthHeader(implicit materializer: Materializer): HttpRequest => Future[HttpRequest] =
    withSimpleAuthHeader(callback = None)

  def withSimpleAuthHeader(callback: Option[String])(
      implicit materializer: Materializer): HttpRequest => Future[HttpRequest] = { request =>
    val simpleRequest = request.withEntity(HttpEntity.Empty)
    val f = withAuthHeader(callback)
    f(simpleRequest)
  }

}

object AuthClient extends LazyLogging {

  def apply(): AuthClient =
    initializeOAuth().recoverWith {
      case cause: IllegalStateException =>
        logger.warn("Couldn't initialize consumer/access client, looking for bearer client instead...", cause)
        initializeBearer()
    } match {
      case Success(client) => client
      case Failure(cause) =>
        throw new IllegalArgumentException(
          "Unable to initialize consumer/access client, or bearer client. Please check your environment variables and/or configuration files",
          cause)
    }

  private def initializeOAuth(): Try[OAuthClient] = Try {
    val consumerToken = ConsumerToken(key = consumerTokenKey, secret = consumerTokenSecret)
    val accessToken = AccessToken(key = accessTokenKey, secret = accessTokenSecret)
    oauth(consumerToken, accessToken)
  }
  private def initializeBearer(): Try[BearerAuthClient] = Try {
    bearer(BearerToken(secret = bearerToken))
  }

  def oauth(consumerToken: ConsumerToken, accessToken: AccessToken): OAuthClient =
    new OAuthClient(consumerToken, accessToken)

  def bearer(bearerToken: BearerToken): BearerAuthClient =
    new BearerAuthClient(bearerToken)
}

package com.danielasfregola.twitter4s.http.oauth

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.stream.Materializer
import com.danielasfregola.twitter4s.entities.BearerToken

import scala.concurrent.Future

private[twitter4s] class BearerAuthClient(bearerToken: BearerToken) extends AuthClient {

  private val authorizationHeader = Authorization(OAuth2BearerToken(bearerToken.secret))

  def withAuthHeader(callback: Option[String])(
      implicit materializer: Materializer): HttpRequest => Future[HttpRequest] = { request =>
    implicit val ec = materializer.executionContext
    Future(request.withHeaders(request.headers :+ authorizationHeader))
  }
}

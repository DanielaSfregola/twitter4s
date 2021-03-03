package com.danielasfregola.twitter4s.http.clients

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.stream.Materializer

import scala.concurrent.Future

private[twitter4s] class BearerAuthClient(token: String) extends CommonClient {

  private val authorizationHeader = Authorization(OAuth2BearerToken(token))

  override def withLogRequest: Boolean = false
  override def withLogRequestResponse: Boolean = false

  def withAuthHeader(callback: Option[String])(
      implicit materializer: Materializer): HttpRequest => Future[HttpRequest] = { request =>
    implicit val ec = materializer.executionContext
    Future(request.withHeaders(request.headers :+ authorizationHeader))
  }
}

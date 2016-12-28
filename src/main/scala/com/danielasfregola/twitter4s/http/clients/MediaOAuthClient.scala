package com.danielasfregola.twitter4s.http.clients

import akka.http.scaladsl.model.HttpRequest

import scala.concurrent.Future

private[twitter4s] trait MediaOAuthClient extends OAuthClient {

  override val withLogRequest: Boolean = true
  override val withLogRequestResponse: Boolean = false

}



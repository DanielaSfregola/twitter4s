package com.danielasfregola.twitter4s.http.clients.rest

import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.Client
import com.danielasfregola.twitter4s.http.oauth.OAuth2Provider

private[twitter4s] class RestClient(val consumerToken: ConsumerToken, val accessToken: AccessToken) extends Client {

  lazy val oauthProvider = new OAuth2Provider(consumerToken, Some(accessToken))
}

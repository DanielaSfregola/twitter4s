package com.danielasfregola.twitter4s.http.clients.authentication

import com.danielasfregola.twitter4s.entities.ConsumerToken
import com.danielasfregola.twitter4s.http.clients.Client
import com.danielasfregola.twitter4s.http.oauth.OAuth2Provider

private[twitter4s] class AuthenticationClient(val consumerToken: ConsumerToken) extends Client {

  lazy val oauthProvider = new OAuth2Provider(consumerToken, None)

}

package com.danielasfregola.twitter4s

import com.danielasfregola.twitter4s.entities.ConsumerToken
import com.danielasfregola.twitter4s.http.clients.authentication.AuthenticationClient
import com.danielasfregola.twitter4s.http.clients.authentication.oauth.TwitterOAuthClient
import com.danielasfregola.twitter4s.util.Configurations._

/** Represents the functionalities offered by the Twitter REST API for authentication
  */
class TwitterAuthenticationClient(val consumerToken: ConsumerToken) extends TwitterAuthClients {

  protected val authenticationClient = new AuthenticationClient(consumerToken)

}

trait TwitterAuthClients extends TwitterOAuthClient

object TwitterAuthenticationClient {

  def apply(): TwitterAuthenticationClient = {
    val consumerToken = ConsumerToken(key = consumerTokenKey, secret = consumerTokenSecret)
    apply(consumerToken)
  }

  def apply(consumerToken: ConsumerToken): TwitterAuthenticationClient =
    new TwitterAuthenticationClient(consumerToken)
}

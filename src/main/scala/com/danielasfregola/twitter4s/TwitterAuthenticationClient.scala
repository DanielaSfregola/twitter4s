package com.danielasfregola.twitter4s

import akka.actor.ActorSystem
import com.danielasfregola.twitter4s.entities.ConsumerToken
import com.danielasfregola.twitter4s.http.clients.authentication.AuthenticationClient
import com.danielasfregola.twitter4s.http.clients.authentication.oauth.TwitterOAuthClient
import com.danielasfregola.twitter4s.util.Configurations._
import com.danielasfregola.twitter4s.util.SystemShutdown

/** Represents the functionalities offered by the Twitter REST API for authentication
  */
class TwitterAuthenticationClient private (val consumerToken: ConsumerToken)(implicit _system: ActorSystem =
                                                                               ActorSystem("twitter4s-auth"))
    extends TwitterAuthClients
    with SystemShutdown {

  protected val system = _system

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

  def withActorSystem(system: ActorSystem): TwitterAuthenticationClient = {
    val consumerToken = ConsumerToken(key = consumerTokenKey, secret = consumerTokenSecret)
    withActorSystem(consumerToken)(system)
  }

  def withActorSystem(consumerToken: ConsumerToken)(system: ActorSystem): TwitterAuthenticationClient =
    new TwitterAuthenticationClient(consumerToken)(system)
}

package com.danielasfregola.twitter4s

import akka.actor.ActorSystem
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.streaming.StreamingClient
import com.danielasfregola.twitter4s.http.clients.streaming.sites.TwitterSiteClient
import com.danielasfregola.twitter4s.http.clients.streaming.statuses.TwitterStatusClient
import com.danielasfregola.twitter4s.http.clients.streaming.users.TwitterUserClient
import com.danielasfregola.twitter4s.util.Configurations._
import com.danielasfregola.twitter4s.util.SystemShutdown

/** Represents the functionalities offered by the Twitter Streaming API
  */
class TwitterStreamingClient private (val consumerToken: ConsumerToken, val accessToken: AccessToken)(
    implicit _system: ActorSystem = ActorSystem("twitter4s-streaming"))
    extends StreamingClients
    with SystemShutdown {

  protected val system = _system

  protected val streamingClient = new StreamingClient(consumerToken, accessToken)

}

trait StreamingClients extends TwitterStatusClient with TwitterUserClient with TwitterSiteClient

object TwitterStreamingClient {

  def apply(): TwitterStreamingClient = {
    val consumerToken = ConsumerToken(key = consumerTokenKey, secret = consumerTokenSecret)
    val accessToken = AccessToken(key = accessTokenKey, secret = accessTokenSecret)
    apply(consumerToken, accessToken)
  }

  def apply(consumerToken: ConsumerToken, accessToken: AccessToken): TwitterStreamingClient =
    new TwitterStreamingClient(consumerToken, accessToken)

  def withActorSystem(system: ActorSystem): TwitterStreamingClient = {
    val consumerToken = ConsumerToken(key = consumerTokenKey, secret = consumerTokenSecret)
    val accessToken = AccessToken(key = accessTokenKey, secret = accessTokenSecret)
    withActorSystem(consumerToken, accessToken)(system)
  }

  def withActorSystem(consumerToken: ConsumerToken, accessToken: AccessToken)(
      system: ActorSystem): TwitterStreamingClient =
    new TwitterStreamingClient(consumerToken, accessToken)(system)
}

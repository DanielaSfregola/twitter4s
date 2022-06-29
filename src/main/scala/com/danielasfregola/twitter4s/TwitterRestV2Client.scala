package com.danielasfregola.twitter4s

import akka.actor.ActorSystem
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.{
  TwitterSearchTweetsClient,
  TwitterTimelinesClient,
  TwitterTweetLookupClient
}
import com.danielasfregola.twitter4s.http.clients.rest.v2.users.{TwitterUserFollowsClient, TwitterUserLookupClient}
import com.danielasfregola.twitter4s.util.Configurations._
import com.danielasfregola.twitter4s.util.SystemShutdown

/** Represents the functionalities offered by the V2 Twitter REST API
  */
class TwitterRestV2Client(val consumerToken: ConsumerToken, val accessToken: AccessToken)(
    implicit _system: ActorSystem = ActorSystem("twitter4s-rest-v2"))
    extends V2RestClients
    with SystemShutdown {

  protected val system = _system

  protected val restClient = new RestClient(consumerToken, accessToken)

}

trait V2RestClients
    extends TwitterTimelinesClient
    with TwitterTweetLookupClient
    with TwitterSearchTweetsClient
    with TwitterUserLookupClient
    with TwitterUserFollowsClient

object TwitterRestV2Client {

  def apply(): TwitterRestV2Client = {
    val consumerToken = ConsumerToken(key = consumerTokenKey, secret = consumerTokenSecret)
    val accessToken = AccessToken(key = accessTokenKey, secret = accessTokenSecret)
    apply(consumerToken, accessToken)
  }

  def apply(consumerToken: ConsumerToken, accessToken: AccessToken): TwitterRestV2Client =
    new TwitterRestV2Client(consumerToken, accessToken)

  def withActorSystem(system: ActorSystem): TwitterRestV2Client = {
    val consumerToken = ConsumerToken(key = consumerTokenKey, secret = consumerTokenSecret)
    val accessToken = AccessToken(key = accessTokenKey, secret = accessTokenSecret)
    withActorSystem(consumerToken, accessToken)(system)
  }

  def withActorSystem(consumerToken: ConsumerToken, accessToken: AccessToken)(
      system: ActorSystem): TwitterRestV2Client =
    new TwitterRestV2Client(consumerToken, accessToken)(system)

}

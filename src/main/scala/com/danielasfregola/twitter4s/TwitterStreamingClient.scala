package com.danielasfregola.twitter4s

import akka.actor.{ActorRefFactory, ActorSystem}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.streaming.statuses.TwitterStatusClient
import com.danielasfregola.twitter4s.util.TokensFromConfig

class TwitterStreamingClient(val consumerToken: ConsumerToken, val accessToken: AccessToken)
                       (implicit val actorRefFactory: ActorRefFactory = ActorSystem("twitter4s")) extends StreamingClients

trait StreamingClients extends TwitterStatusClient

object TwitterStreamingClient {

  def apply(): TwitterStreamingClient = {
    val consumerToken = ConsumerToken(key = TokensFromConfig.consumerTokenKey, secret = TokensFromConfig.consumerTokenSecret)
    val accessToken = AccessToken(key = TokensFromConfig.accessTokenKey, secret = TokensFromConfig.accessTokenSecret)
    new TwitterStreamingClient(consumerToken, accessToken)
  }
}

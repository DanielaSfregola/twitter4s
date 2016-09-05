package com.danielasfregola.twitter4s

import akka.actor.{ActorRef, ActorRefFactory, ActorSystem, Props}
import com.danielasfregola.twitter4s.entities.streaming.StreamingUpdate
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.TwitterStreamListener
import com.danielasfregola.twitter4s.http.clients.streaming.statuses.TwitterStatusClient
import com.danielasfregola.twitter4s.http.clients.streaming.users.TwitterUserClient
import com.danielasfregola.twitter4s.util.TokensFromConfig

class TwitterStreamingClient(val consumerToken: ConsumerToken, val accessToken: AccessToken)
                            (implicit val actorRefFactory: ActorRefFactory = ActorSystem("twitter4s-streaming")) extends StreamingClients {

  private[twitter4s] def createListener(f: StreamingUpdate => Unit): ActorRef =
    actorRefFactory.actorOf(Props(new TwitterStreamListener { def processStreamingUpdate = f }))
}

trait StreamingClients extends TwitterStatusClient with TwitterUserClient

object TwitterStreamingClient {

  def apply: TwitterStreamingClient = {
    val consumerToken = ConsumerToken(key = TokensFromConfig.consumerTokenKey, secret = TokensFromConfig.consumerTokenSecret)
    val accessToken = AccessToken(key = TokensFromConfig.accessTokenKey, secret = TokensFromConfig.accessTokenSecret)
    new TwitterStreamingClient(consumerToken, accessToken)
  }
}

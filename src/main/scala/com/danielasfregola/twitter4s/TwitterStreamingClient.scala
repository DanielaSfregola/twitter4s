package com.danielasfregola.twitter4s

import akka.actor.{ActorRef, ActorRefFactory, ActorSystem, Props}
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.TwitterStreamListener
import com.danielasfregola.twitter4s.http.clients.streaming.sites.TwitterSiteClient
import com.danielasfregola.twitter4s.http.clients.streaming.statuses.TwitterStatusClient
import com.danielasfregola.twitter4s.http.clients.streaming.users.TwitterUserClient
import com.danielasfregola.twitter4s.util.Configurations

class TwitterStreamingClient(val consumerToken: ConsumerToken, val accessToken: AccessToken)
                            (implicit val system: ActorSystem = ActorSystem("twitter4s-streaming")) extends StreamingClients

trait StreamingClients extends TwitterStatusClient
  with TwitterUserClient
  with TwitterSiteClient

object TwitterStreamingClient extends Configurations {

  def apply(): TwitterStreamingClient = {
    val consumerToken = ConsumerToken(key = consumerTokenKey, secret = consumerTokenSecret)
    val accessToken = AccessToken(key = accessTokenKey, secret = accessTokenSecret)
    new TwitterStreamingClient(consumerToken, accessToken)
  }
}

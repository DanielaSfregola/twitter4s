package com.danielasfregola.twitter4s

import java.util.UUID

import akka.actor.ActorSystem
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.danielasfregola.twitter4s.http.clients.streaming.sites.TwitterSiteClient
import com.danielasfregola.twitter4s.http.clients.streaming.statuses.TwitterStatusClient
import com.danielasfregola.twitter4s.http.clients.streaming.users.TwitterUserClient
import com.danielasfregola.twitter4s.util.Configurations

class TwitterStreamingClient(val consumerToken: ConsumerToken, val accessToken: AccessToken)
                            (implicit val system: ActorSystem) extends StreamingClients

trait StreamingClients
  extends TwitterStatusClient
  with TwitterUserClient
  with TwitterSiteClient

object TwitterStreamingClient extends Configurations {

  def apply(system: ActorSystem = ActorSystem(s"twitter4s-streaming-${UUID.randomUUID}")): TwitterStreamingClient = {
    val consumerToken = ConsumerToken(key = consumerTokenKey, secret = consumerTokenSecret)
    val accessToken = AccessToken(key = accessTokenKey, secret = accessTokenSecret)
    apply(consumerToken, accessToken, system)
  }

  def apply(consumerToken: ConsumerToken, accessToken: AccessToken): TwitterStreamingClient =
    apply(consumerToken, accessToken, ActorSystem(s"twitter4s-streaming-${UUID.randomUUID}"))

  def apply(consumerToken: ConsumerToken, accessToken: AccessToken, system: ActorSystem): TwitterStreamingClient =
    new TwitterStreamingClient(consumerToken, accessToken)(system)
}

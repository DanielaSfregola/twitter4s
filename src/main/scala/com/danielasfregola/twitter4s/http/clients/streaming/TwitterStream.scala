package com.danielasfregola.twitter4s.http.clients.streaming

import akka.actor.ActorSystem
import akka.stream.SharedKillSwitch
import com.danielasfregola.twitter4s.StreamingClients
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

class TwitterStream(private val killSwitch: SharedKillSwitch)
                   (val consumerToken: ConsumerToken, val accessToken: AccessToken, val system: ActorSystem) extends StreamingClients {

  def close() = killSwitch.shutdown()

  override protected def preProcessing(): Unit = close()
}

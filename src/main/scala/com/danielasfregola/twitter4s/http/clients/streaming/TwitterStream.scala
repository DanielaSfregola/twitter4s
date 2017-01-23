package com.danielasfregola.twitter4s.http.clients.streaming

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.stream.KillSwitch
import com.danielasfregola.twitter4s.StreamingClients
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

/** It represent a twitter stream operation. It can be used to close the stream on demand
  * or to replace the current stream with another twitter stream.
  */
case class TwitterStream(private val killSwitch: KillSwitch, private val request: HttpRequest)(
    val consumerToken: ConsumerToken,
    val accessToken: AccessToken,
    val system: ActorSystem)
    extends StreamingClients {

  def close() = {
    log.info(s"${request.method.value} ${request.uri}: closing streaming")
    killSwitch.shutdown()
  }

  override protected def preProcessing(): Unit = close()
}

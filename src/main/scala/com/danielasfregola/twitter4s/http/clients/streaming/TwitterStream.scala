package com.danielasfregola.twitter4s.http.clients.streaming

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.stream.KillSwitch
import com.danielasfregola.twitter4s.StreamingClients
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future

/** Represents a twitter stream operation. It can be used to close the stream on demand
  * or to replace the current stream with another twitter stream.
  */
final case class TwitterStream(consumerToken: ConsumerToken, accessToken: AccessToken)(
    private val killSwitch: KillSwitch,
    private val request: HttpRequest,
    private val system: ActorSystem)
    extends StreamingClients
    with LazyLogging {

  protected val streamingClient = new StreamingClient(consumerToken, accessToken)(system) {
    override def preProcessing(): Unit = {
      super.preProcessing()
      close()
    }
  }

  /** Closes a stream that is open and processing data.
    *
    * @return : Future that will be completed with Unit once the stream has been terminated.
    */
  def close(): Future[Unit] = Future.successful {
    logger.info(s"${request.method.value} ${request.uri}: closing streaming")
    killSwitch.shutdown
  }
}

package com.danielasfregola.twitter4s.http.clients.streaming
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model.HttpRequest
import akka.stream.Materializer
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}

import scala.concurrent.ExecutionContext

class ActorStreamingClientImpl(consumerToken: ConsumerToken,
                               accessToken: AccessToken,
                               request: HttpRequest,
                               handleStreamErrors: Boolean = true)(override implicit val actorSystem: ActorSystem,
                                                                   override implicit val mat: Materializer,
                                                                   override implicit val ec: ExecutionContext)
    extends ActorStreamingClient(consumerToken, accessToken, request, handleStreamErrors)(actorSystem, mat, ec)
    with HttpConnectionFlow {}

object ActorStreamingClientImpl {
  def props(consumerToken: ConsumerToken,
            accessToken: AccessToken,
            request: HttpRequest)(implicit actorSystem: ActorSystem, mat: Materializer, ec: ExecutionContext) =
    Props(new ActorStreamingClientImpl(consumerToken = consumerToken, accessToken = accessToken, request = request))
}

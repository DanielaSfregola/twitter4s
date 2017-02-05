package com.danielasfregola.twitter4s.helpers.streaming

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.pattern.ask
import akka.stream.{KillSwitches, Materializer, SharedKillSwitch}
import akka.util.Timeout
import akka.util.Timeout.durationToTimeout
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.helpers.{RequestDSL, Spec}
import com.danielasfregola.twitter4s.http.clients.streaming.StreamingClient

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

trait ClientSpec extends Spec {

  def dummyProcessing: PartialFunction[StreamingMessage, Unit] = { case _ => }

  abstract class ClientSpecContext extends RequestDSL with SpecContext {

    val killSwitch = KillSwitches.shared(s"test-twitter4s-${UUID.randomUUID}")

    protected val streamingClient = new StreamingClient(consumerToken, accessToken) {

      override def processStreamRequest[T <: StreamingMessage : Manifest](request: HttpRequest, killSwitch: SharedKillSwitch)
                                                                         (f: PartialFunction[T, Unit])
                                                                         (implicit system: ActorSystem, materializer: Materializer): Future[SharedKillSwitch] = {
        implicit val ec = materializer.executionContext
        implicit val timeout: Timeout = DurationInt(20) seconds
        val responseR: Future[HttpResponse] = (transport.ref ? request).map(_.asInstanceOf[HttpResponse])
        for {
          response <- responseR
          _ <- Future.successful(processBody(response, killSwitch)(f)(manifest[T], request, materializer))
        } yield killSwitch
      }

    }
  }
}

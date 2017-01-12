package com.danielasfregola.twitter4s.util.streaming

import java.util.UUID

import akka.http.scaladsl.model._
import akka.pattern.ask
import akka.stream.{KillSwitches, SharedKillSwitch}
import akka.util.Timeout
import akka.util.Timeout.durationToTimeout
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.http.clients.streaming.StreamingClient
import com.danielasfregola.twitter4s.util.{RequestDSL, Spec}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

abstract class ClientSpec extends Spec {

  def dummyProcessing: PartialFunction[StreamingMessage, Unit] = { case _ => }

  abstract class ClientSpecContext extends RequestDSL with StreamingClient with SpecContext {

    val killSwitch = KillSwitches.shared(s"test-twitter4s-${UUID.randomUUID}")

    override def processStreamRequest[T <: StreamingMessage: Manifest](request: HttpRequest, killSwitch: SharedKillSwitch)(f: PartialFunction[T, Unit]): Future[SharedKillSwitch] = {
      implicit val timeout: Timeout = DurationInt(20) seconds
      val responseR: Future[HttpResponse] = (transport.ref ? request).map(_.asInstanceOf[HttpResponse])
      for {
        response <- responseR
        _ <- Future.successful(processBody(response, killSwitch)(f)(manifest[T], request))
      } yield killSwitch
    }

  }

}

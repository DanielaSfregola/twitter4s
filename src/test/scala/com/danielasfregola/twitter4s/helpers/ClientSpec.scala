package com.danielasfregola.twitter4s.helpers

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.pattern.ask
import akka.stream.{KillSwitches, Materializer, SharedKillSwitch}
import akka.util.Timeout
import akka.util.Timeout.durationToTimeout
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.http.clients.authentication.AuthenticationClient
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.streaming.StreamingClient

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

trait ClientSpec extends Spec {

  abstract class AuthenticationClientSpecContext extends RequestDSL with SpecContext {

    protected val authenticationClient = new AuthenticationClient(consumerToken) {

      override def sendAndReceive[T](request: HttpRequest, f: HttpResponse => Future[T])(
          implicit system: ActorSystem,
          materializer: Materializer): Future[T] = {
        implicit val ec = materializer.executionContext
        implicit val timeout: Timeout = DurationInt(20) seconds
        val requestStartTime = System.currentTimeMillis
        val responseR: Future[HttpResponse] = (transport.ref ? request).map(_.asInstanceOf[HttpResponse])
        for {
          response <- responseR
          t <- unmarshal[T](requestStartTime, f)(request, response, materializer)
        } yield t
      }
    }
  }

  abstract class RestClientSpecContext extends RequestDSL with SpecContext {

    protected val restClient = new RestClient(consumerToken, accessToken) {

      override def sendAndReceive[T](request: HttpRequest, f: HttpResponse => Future[T])(
          implicit system: ActorSystem,
          materializer: Materializer): Future[T] = {
        implicit val ec = materializer.executionContext
        implicit val timeout: Timeout = DurationInt(20) seconds
        val requestStartTime = System.currentTimeMillis
        val responseR: Future[HttpResponse] = (transport.ref ? request).map(_.asInstanceOf[HttpResponse])
        for {
          response <- responseR
          t <- unmarshal[T](requestStartTime, f)(request, response, materializer)
        } yield t
      }
    }
  }

  abstract class StreamingClientSpecContext extends RequestDSL with SpecContext {

    def dummyProcessing: PartialFunction[StreamingMessage, Unit] = { case _ => }

    val killSwitch = KillSwitches.shared(s"test-twitter4s-${UUID.randomUUID}")

    protected val streamingClient = new StreamingClient(consumerToken, accessToken) {

      override def processStreamRequest[T <: StreamingMessage: Manifest](
          request: HttpRequest
      )(
          f: PartialFunction[T, Unit],
          errorHandler: PartialFunction[Throwable, Unit]
      )(
          implicit
          system: ActorSystem,
          materializer: Materializer
      ): Future[SharedKillSwitch] = {
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

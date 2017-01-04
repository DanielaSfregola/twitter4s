package com.danielasfregola.twitter4s.util.streaming

import akka.http.scaladsl.model._
import akka.pattern.ask
import akka.util.Timeout
import akka.util.Timeout.durationToTimeout
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.http.clients.StreamingClient
import com.danielasfregola.twitter4s.util.{RequestDSL, Spec}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class ClientSpec extends Spec {

  def dummyProcessing: PartialFunction[StreamingMessage, Unit] = { case _ => }

  abstract class ClientSpecContext extends RequestDSL with StreamingClient with SpecContext {

    override def processStreamRequest[T <: StreamingMessage: Manifest](request: HttpRequest)(f: PartialFunction[T, Unit]): Future[Unit] = {
      implicit val timeout: Timeout = DurationInt(20) seconds
      val responseR: Future[HttpResponse] = (transport.ref ? request).map(_.asInstanceOf[HttpResponse])
      for {
        response <- responseR
        _ <- unmarshalStream(response, f)(manifest[T], request)
      } yield ()
    }

  }

}

package com.danielasfregola.twitter4s.helpers.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.pattern.ask
import akka.stream.Materializer
import akka.util.Timeout
import akka.util.Timeout.durationToTimeout
import com.danielasfregola.twitter4s.helpers.{RequestDSL, Spec}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

abstract class ClientSpec extends Spec {

  abstract class ClientSpecContext extends RequestDSL with SpecContext {

    protected val restClient = new RestClient(consumerToken, accessToken) {

      override def sendAndReceive[T](request: HttpRequest, f: HttpResponse => Future[T])
                                    (implicit system: ActorSystem, materializer: Materializer): Future[T] = {
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
}

package com.danielasfregola.twitter4s.util.rest

import akka.http.scaladsl.model._
import akka.pattern.ask
import akka.util.Timeout
import akka.util.Timeout.durationToTimeout
import com.danielasfregola.twitter4s.http.clients.RestClient
import com.danielasfregola.twitter4s.util.{RequestDSL, Spec}

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class ClientSpec extends Spec {

  abstract class ClientSpecContext extends RequestDSL with RestClient with SpecContext {

    override def sendAndReceive[T](request: HttpRequest, f: HttpResponse => Future[T]): Future[T] = {
      implicit val timeout: Timeout = DurationInt(20) seconds
      val requestStartTime =  System.currentTimeMillis
      val responseR: Future[HttpResponse] = (transport.ref ? request).map(_.asInstanceOf[HttpResponse])
      for {
        response <- responseR
        t <- unmarshal[T](requestStartTime, f)(request, response)
      } yield t
    }
  }

}

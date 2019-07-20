package com.danielasfregola.twitter4s.http.clients.streaming
import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.Flow

trait HttpConnectionFlow {

  def getRequest: HttpRequest
  implicit val actorSystem: ActorSystem

  // if anyone has a better idea, I'd love to see it
  protected lazy val connectionFlow: Flow[HttpRequest, HttpResponse, NotUsed] = Http()
    .outgoingConnectionHttps(getRequest.uri.authority.host.toString, getRequest.uri.effectivePort)
    .asInstanceOf[Flow[HttpRequest, HttpResponse, NotUsed]]
}

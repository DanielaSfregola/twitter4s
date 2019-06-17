package com.danielasfregola.twitter4s.http.clients.streaming
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.StreamTcpException
import akka.stream.scaladsl.Flow

import scala.concurrent.Future

trait HttpConnectionFlow {

  def getRequest: HttpRequest
  implicit val actorSystem: ActorSystem

  protected lazy val connectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = Http()
    .outgoingConnectionHttps(getRequest.uri.authority.host.toString, getRequest.uri.effectivePort)


}

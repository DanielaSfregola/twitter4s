package com.danielasfregola.twitter4s.http.clients

import com.danielasfregola.twitter4s.exceptions.{Errors, TwitterException}
import com.danielasfregola.twitter4s.http.unmarshalling.JsonSupport
import com.danielasfregola.twitter4s.providers.ActorSystemProvider
import com.danielasfregola.twitter4s.util.ActorContextExtractor
import org.json4s.native.Serialization

import scala.util.Try
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString

import scala.concurrent.duration._
import scala.concurrent.Future

trait Client extends JsonSupport with ActorContextExtractor { self: ActorSystemProvider =>
  
  def withLogRequest: Boolean
  def withLogRequestResponse: Boolean

  def sendReceiveAs[T: Manifest](httpRequest: HttpRequest): Future[T] = {
    val superUnmarshaller = Unmarshaller.firstOf(json4sUnmarshaller[T])
    sendAndReceive(httpRequest, { response => superUnmarshaller.apply(response.entity) })
  }

  protected def sendAndReceive[T](request: HttpRequest, f: HttpResponse => Future[T]): Future[T] = {
    implicit val _ = request

    val requestStartTime =  System.currentTimeMillis
    if (withLogRequest) logRequest(request)
    Source
    .single(request)
    .via(connection)
    .mapAsync(1) { implicit response => unmarshal(requestStartTime, f) }
    .runWith(Sink.head)
  }

  private def connection(implicit request: HttpRequest) = {
    val scheme = request.uri.scheme
    val host = request.uri.authority.host.toString
    val port = request.uri.effectivePort
    if (scheme == "https") Http().outgoingConnectionHttps(host, port)
    else Http().outgoingConnection(host, port)
  }

  protected def unmarshal[T](requestStartTime: Long, f: HttpResponse => Future[T])(implicit request: HttpRequest, response: HttpResponse) = {
    if (withLogRequestResponse) logRequestResponse(requestStartTime)

    if (response.status.isSuccess) f(response)
    else {
      response.entity.toStrict(50 seconds).map { sink =>
        val body = sink.data.utf8String
        val errors = Try {
          Serialization.read[Errors](body)
        } getOrElse Errors(body)
        throw new TwitterException(response.status, errors)
      }
    }
  }

  // TODO - logRequest, logRequestResponse customisable?
  def logRequest: HttpRequest => HttpRequest = { request =>
    log.info("{} {}", request.method, request.uri)
    log.debug("{} {} | {} | {}", request.method, request.uri, request.entity, request)
    request
  }

  def logRequestResponse(requestStartTime: Long)(implicit request: HttpRequest): HttpResponse => HttpResponse = { response =>
    val elapsed = System.currentTimeMillis - requestStartTime
    log.info("{} {} ({}) | {}ms", request.method, request.uri, response.status, elapsed)
    log.debug("{} {} ({}) | {}", request.method, request.uri, response.status, response.entity)
    response
  }
}

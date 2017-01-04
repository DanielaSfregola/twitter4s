package com.danielasfregola.twitter4s.http.clients

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, HttpRequest, HttpResponse}
import com.danielasfregola.twitter4s.exceptions.{Errors, TwitterException}
import com.danielasfregola.twitter4s.http.serializers.JsonSupport
import com.danielasfregola.twitter4s.providers.ActorSystemProvider
import com.danielasfregola.twitter4s.util.ActorContextExtractor
import org.json4s.native.Serialization

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Try

trait CommonClient extends JsonSupport with ActorContextExtractor { self: ActorSystemProvider =>
  
  def withLogRequest: Boolean
  def withLogRequestResponse: Boolean

  protected def connection(implicit request: HttpRequest) = {
    val scheme = request.uri.scheme
    val host = request.uri.authority.host.toString
    val port = request.uri.effectivePort
    if (scheme == "https") Http().outgoingConnectionHttps(host, port)
    else Http().outgoingConnection(host, port)
  }

  protected def unmarshal[T](requestStartTime: Long, f: HttpResponse => Future[T])(implicit request: HttpRequest, response: HttpResponse) = {
    if (withLogRequestResponse) logRequestResponse(requestStartTime)

    if (response.status.isSuccess) f(response)
    else parseFailedResponse(response)
  }

  private def parseFailedResponse(response: HttpResponse) =
    response.entity.toStrict(50 seconds).map { sink =>
      val body = sink.data.utf8String
      val errors = Try {
        Serialization.read[Errors](body)
      } getOrElse Errors(body)
      throw new TwitterException(response.status, errors)
    }

  // TODO - logRequest, logRequestResponse customisable?
  def logRequest(implicit request: HttpRequest): HttpRequest = {
    log.info("{} {}", request.method.value, request.uri)
    if (log.isDebugEnabled) {
      for {
        requestBody <- toBody(request.entity)
      } yield log.debug("{} {} | {} | {}", request.method.value, request.uri, requestBody)
    }
    request
  }

  def logRequestResponse(requestStartTime: Long)(implicit request: HttpRequest): HttpResponse => HttpResponse = { response =>
    val elapsed = System.currentTimeMillis - requestStartTime
    log.info("{} {} ({}) | {}ms", request.method.value, request.uri, response.status, elapsed)
    if (log.isDebugEnabled) {
      for {
        responseBody <- toBody(response.entity)
      } yield log.debug(s"{} {} ({}) | {} | $responseBody ", request.method.value, request.uri, response.status, response.headers.mkString(", "))
    }
    response
  }

  private def toBody(entity: HttpEntity): Future[String] = entity.toStrict(5 seconds).map(_.data.decodeString("UTF-8"))
}

package com.danielasfregola.twitter4s.http.clients

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, HttpRequest, HttpResponse}
import akka.stream.Materializer
import com.danielasfregola.twitter4s.exceptions.{Errors, TwitterException}
import com.danielasfregola.twitter4s.http.serializers.JsonSupport
import com.typesafe.scalalogging.LazyLogging
import org.json4s.native.Serialization

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Try

private[twitter4s] trait CommonClient extends JsonSupport with LazyLogging {

  def withLogRequest: Boolean
  def withLogRequestResponse: Boolean

  protected def connection(implicit request: HttpRequest, system: ActorSystem) = {
    val scheme = request.uri.scheme
    val host = request.uri.authority.host.toString
    val port = request.uri.effectivePort
    if (scheme == "https") Http().outgoingConnectionHttps(host, port)
    else Http().outgoingConnection(host, port)
  }

  protected def unmarshal[T](requestStartTime: Long, f: HttpResponse => Future[T])(implicit request: HttpRequest,
                                                                                   response: HttpResponse,
                                                                                   materializer: Materializer) = {
    implicit val ec = materializer.executionContext
    if (withLogRequestResponse) logRequestResponse(requestStartTime)

    if (response.status.isSuccess) f(response)
    else parseFailedResponse(response).flatMap(Future.failed)
  }

  protected def parseFailedResponse(response: HttpResponse)(implicit materializer: Materializer) = {
    implicit val ec = materializer.executionContext
    response.entity.toStrict(50 seconds).map { sink =>
      val body = sink.data.utf8String
      val errors = Try {
        Serialization.read[Errors](body)
      } getOrElse Errors(body)
      TwitterException(response.status, errors)
    }
  }

  // TODO - logRequest, logRequestResponse customisable?
  def logRequest(implicit request: HttpRequest, materializer: Materializer): HttpRequest = {
    implicit val ec = materializer.executionContext
    logger.info(s"${request.method.value} ${request.uri}")
    if (logger.underlying.isDebugEnabled) {
      for {
        requestBody <- toBody(request.entity)
      } yield logger.debug(s"${request.method.value} ${request.uri} | $requestBody")
    }
    request
  }

  def logRequestResponse(requestStartTime: Long)(implicit request: HttpRequest,
                                                 materializer: Materializer): HttpResponse => HttpResponse = {
    response =>
      implicit val ec = materializer.executionContext
      val elapsed = System.currentTimeMillis - requestStartTime
      logger.info(s"${request.method.value} ${request.uri} (${response.status}) | ${elapsed}ms")
      if (logger.underlying.isDebugEnabled) {
        for {
          responseBody <- toBody(response.entity)
        } yield
          logger.debug(
            s"${request.method.value} ${request.uri} (${response.status}) | ${response.headers.mkString(", ")} | $responseBody")
      }
      response
  }

  private def toBody(entity: HttpEntity)(implicit materializer: Materializer): Future[String] = {
    implicit val ec = materializer.executionContext
    entity.toStrict(5 seconds).map(_.data.decodeString("UTF-8"))
  }
}

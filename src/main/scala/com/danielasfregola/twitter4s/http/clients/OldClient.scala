package com.danielasfregola.twitter4s.http.clients

import com.danielasfregola.twitter4s.exceptions.{Errors, OldTwitterException}
import org.json4s.native.Serialization
import spray.client.pipelining._

import scala.concurrent.Future
import scala.util.Try
import spray.http._
import spray.httpx.unmarshalling.{FromResponseUnmarshaller, UnmarshallerLifting}
import com.danielasfregola.twitter4s.http.unmarshalling.OldJsonSupport
import com.danielasfregola.twitter4s.providers.ActorSystemProvider
import com.danielasfregola.twitter4s.util.ActorContextExtractor


trait OldClient extends OldJsonSupport with ActorContextExtractor with UnmarshallerLifting {
  self: ActorSystemProvider =>

  private[twitter4s] implicit class RichHttpRequest(val request: HttpRequest) {
    def respondAs[T: Manifest]: Future[T] = sendReceiveAs[T](request)
  }

  // TODO - logRequest, logResponse customisable?
  def pipeline[T: FromResponseUnmarshaller]: HttpRequest => Future[T]

  private[twitter4s] def sendReceiveAs[T: FromResponseUnmarshaller](request: HttpRequest) =
    pipeline apply request

  private[twitter4s] def sendReceive = spray.client.pipelining.sendReceive

  def logRequest: HttpRequest => HttpRequest = { request =>
    log.info("{} {}", request.method, request.uri)
    log.debug("{} {} | {} | {}", request.method, request.uri, request.entity.asString, request)
    request
  }

  def logResponse(requestStartTime: Long)(implicit request: HttpRequest): HttpResponse => HttpResponse = { response =>
    val elapsed = System.currentTimeMillis - requestStartTime
    log.info("{} {} ({}) | {}ms", request.method, request.uri, response.status, elapsed)
    log.debug("{} {} ({}) | {}", request.method, request.uri, response.status, response.entity.asString)
    response
  }

  private[twitter4s] def unmarshalResponse[T: FromResponseUnmarshaller]: HttpResponse ⇒ T = { hr =>
    hr.status.isSuccess match {
      case true => hr ~> unmarshal[T]
      case false =>
        val errors = Try {
          Serialization.read[Errors](hr.entity.asString)
        } getOrElse { Errors() }
        throw new OldTwitterException(hr.status, errors)
    }
  }

  private[twitter4s] def unmarshalEmptyResponse: HttpResponse ⇒ Unit = { hr =>
    if (hr.status.isFailure) {
      val errors = Try {
        Serialization.read[Errors](hr.entity.asString)
      } getOrElse { Errors() }
      throw new OldTwitterException(hr.status, errors)
    }
  }
}

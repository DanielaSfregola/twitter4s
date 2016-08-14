package com.danielasfregola.twitter4s.http.clients

import com.danielasfregola.twitter4s.exceptions.{TwitterException, Errors}
import org.json4s.native.Serialization
import spray.client.pipelining._

import scala.concurrent.Future
import scala.util.Try

import spray.http._
import spray.httpx.unmarshalling.{FromResponseUnmarshaller, UnmarshallerLifting}

import com.danielasfregola.twitter4s.http.unmarshalling.JsonSupport
import com.danielasfregola.twitter4s.providers.ActorRefFactoryProvider
import com.danielasfregola.twitter4s.util.ActorContextExtractor


trait Client extends JsonSupport with ActorContextExtractor with UnmarshallerLifting {
  self: ActorRefFactoryProvider =>

  implicit class RichHttpRequest(val request: HttpRequest) {
    def respondAs[T: Manifest]: Future[T] = sendReceiveAs[T](request)
  }

  // TODO - logRequest, logResponse customisable?
  def pipeline[T: FromResponseUnmarshaller]: HttpRequest => Future[T]

  def sendReceiveAs[T: FromResponseUnmarshaller](request: HttpRequest) =
    pipeline apply request

  def sendReceive = spray.client.pipelining.sendReceive

  def logRequest: HttpRequest => HttpRequest = { request =>
    log.info(s"${request.method} ${request.uri}")
    log.debug(s"${request.method} ${request.uri} | ${request.entity.asString} | ${request.headers} | $request")
    request
  }

  def logResponse(requestStartTime: Long)(implicit request: HttpRequest): HttpResponse => HttpResponse = { response =>
    val elapsed = System.currentTimeMillis - requestStartTime
    log.info(s"${request.method} ${request.uri} (${response.status}) | ${elapsed}ms")
    log.debug(s"${request.method} ${request.uri} (${response.status}) | ${response.entity.asString}")
    response
  }

  protected def unmarshalResponse[T: FromResponseUnmarshaller]: HttpResponse ⇒ T = { hr =>
    hr.status.isSuccess match {
      case true => hr ~> unmarshal[T]
      case false =>
        val errors = Try {
          Serialization.read[Errors](hr.entity.asString)
        } getOrElse { Errors() }
        throw new TwitterException(hr.status, errors)
    }
  }

  def unmarshalEmptyResponse: HttpResponse ⇒ Unit = { hr =>
    hr.status.isSuccess match {
      case true => ()
      case false =>
        val errors = Try {
          Serialization.read[Errors](hr.entity.asString)
        } getOrElse { Errors() }
        throw new TwitterException(hr.status, errors)
    }
  }
}

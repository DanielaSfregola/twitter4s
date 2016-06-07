package com.danielasfregola.twitter4s.http.clients

import com.danielasfregola.twitter4s.exceptions.{Errors, TwitterException}
import org.json4s.native.Serialization
import spray.client.pipelining._
import spray.http._
import spray.httpx.unmarshalling.{Deserializer => _}

import scala.util.Try

trait MediaOAuthClient extends OAuthClient {

  def formDataPipeline = { implicit request: HttpRequest =>
    request ~> (withSimpleOAuthHeader ~> logRequest ~> sendReceive ~> logResponse(System.currentTimeMillis) ~> unmarshalEmptyResponse)
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



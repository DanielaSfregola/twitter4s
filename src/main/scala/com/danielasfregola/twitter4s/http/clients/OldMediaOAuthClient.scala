package com.danielasfregola.twitter4s.http.clients

import spray.client.pipelining._
import spray.http._
import spray.httpx.unmarshalling.{Deserializer => _}

private[twitter4s] trait OldMediaOAuthClient extends OldOAuthClient {

  def formDataPipeline = { implicit request: HttpRequest =>
    request ~> (withSimpleOAuthHeader ~> logRequest ~> sendReceive ~> logResponse(System.currentTimeMillis) ~> unmarshalEmptyResponse)
  }
}



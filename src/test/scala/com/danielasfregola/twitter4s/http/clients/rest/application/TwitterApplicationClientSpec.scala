package com.danielasfregola.twitter4s.http.clients.rest.application

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.enums.Resource
import com.danielasfregola.twitter4s.entities.{RateLimits, RatedData}
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterApplicationClientSpec extends ClientSpec {

  class TwitterApplicationClientSpecContext extends RestClientSpecContext with TwitterApplicationClient

  "Twitter Application Client" should {

    "get application rate limits for some resources" in new TwitterApplicationClientSpecContext {
      val result: RatedData[RateLimits] = when(rateLimits(Resource.Account, Resource.Statuses))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/application/rate_limit_status.json"
          request.uri.rawQueryString === Some("resources=account%2Cstatuses")
        }
        .respondWithRated("/twitter/rest/application/rate_limits.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[RateLimits]("/fixtures/rest/application/rate_limits.json")
    }

    "get application rate limits for all the resources" in new TwitterApplicationClientSpecContext {
      val result: RatedData[RateLimits] = when(rateLimits())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/application/rate_limit_status.json"
          request.uri.rawQueryString === None
        }
        .respondWithRated("/twitter/rest/application/rate_limits.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[RateLimits]("/fixtures/rest/application/rate_limits.json")
    }
  }
}

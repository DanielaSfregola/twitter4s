package com.danielasfregola.twitter4s.http.clients.rest.application


import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.RateLimits
import com.danielasfregola.twitter4s.entities.enums.Resource
import com.danielasfregola.twitter4s.util.ClientSpec

class TwitterApplicationClientSpec extends ClientSpec {

  class TwitterApplicationClientSpecContext extends ClientSpecContext with TwitterApplicationClient

  "Twitter Application Client" should {

    "get application rate limits for some resources" in new TwitterApplicationClientSpecContext {
      val result: RateLimits = when(rateLimits(Resource.Account, Resource.Statuses)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/application/rate_limit_status.json"
        request.uri.queryString() === Some("resources=account,statuses")
      }.respondWith("/twitter/rest/application/rate_limits.json").await
      result === loadJsonAs[RateLimits]("/fixtures/rest/application/rate_limits.json")
    }

    "get application rate limits for all the resources" in new TwitterApplicationClientSpecContext {
      val result: RateLimits = when(rateLimits()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/application/rate_limit_status.json"
        request.uri.queryString() === None
      }.respondWith("/twitter/rest/application/rate_limits.json").await
      result === loadJsonAs[RateLimits]("/fixtures/rest/application/rate_limits.json")
    }
  }
}

package com.danielasfregola.twitter4s.http.clients.rest.application


import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.RateLimits
import com.danielasfregola.twitter4s.entities.enums.Resource

class TwitterApplicationClientSpec extends ClientSpec {

  class TwitterApplicationClientSpecContext extends ClientSpecContext with TwitterApplicationClient

  "Twitter Application Client" should {

    "get application rate limits for some resources" in new TwitterApplicationClientSpecContext {
      val result: RateLimits = when(rateLimits(Resource.Account, Resource.Statuses)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/application/rate_limit_status.json"
        request.uri.query === Query("resources=account,statuses")
      }.respondWith("/twitter/rest/application/rate_limits.json").await
      result === loadJsonAs[RateLimits]("/fixtures/rest/application/rate_limits.json")
    }

    "get application rate limits for all the resources" in new TwitterApplicationClientSpecContext {
      val result: RateLimits = when(rateLimits()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/application/rate_limit_status.json"
        request.uri.query === Query()
      }.respondWith("/twitter/rest/application/rate_limits.json").await
      result === loadJsonAs[RateLimits]("/fixtures/rest/application/rate_limits.json")
    }
  }
}

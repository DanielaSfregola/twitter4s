package com.danielasfregola.twitter4s.http.clients.application

import com.danielasfregola.twitter4s.http.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.RateLimits
import com.danielasfregola.twitter4s.entities.enums.Resource
import com.danielasfregola.twitter4s.http.ClientSpecContext

class TwitterApplicationClientSpec extends ClientSpec {

  trait TwitterApplicationClientSpecContext extends ClientSpecContext with TwitterApplicationClient

  "Twitter Application Client" should {

    "get application rate limits for some resources" in new TwitterApplicationClientSpecContext {
      val result: RateLimits = when(getRateLimits(Resource.Account, Resource.Statuses)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/application/rate_limit_status.json"
        request.uri.query === Query("resources=account,statuses")
      }.respondWith("/twitter/application/rate_limits.json").await
      result === loadJsonAs[RateLimits]("/fixtures/application/rate_limits.json")
    }

    "get application rate limits for all the resources" in new TwitterApplicationClientSpecContext {
      val result: RateLimits = when(getRateLimits()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/application/rate_limit_status.json"
        request.uri.query === Query()
      }.respondWith("/twitter/application/rate_limits.json").await
      result === loadJsonAs[RateLimits]("/fixtures/application/rate_limits.json")
    }
  }
}

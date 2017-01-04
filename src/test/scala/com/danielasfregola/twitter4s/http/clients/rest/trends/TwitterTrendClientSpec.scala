package com.danielasfregola.twitter4s.http.clients.rest.trends

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{Location, LocationTrends}
import com.danielasfregola.twitter4s.util.rest.ClientSpec

class TwitterTrendClientSpec  extends ClientSpec {

  class TwitterTrendClientSpecContext extends ClientSpecContext with TwitterTrendClient

  "Twitter Trend Client" should {

    "get global trends" in new TwitterTrendClientSpecContext {
      val result: Seq[LocationTrends] = when(globalTrends()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/trends/place.json"
        request.uri.queryString() === Some("id=1")
      }.respondWith("/twitter/rest/trends/trends.json").await
      result === loadJsonAs[Seq[LocationTrends]]("/fixtures/rest/trends/trends.json")
    }

    "get trends for a location" in new TwitterTrendClientSpecContext {
      val result: Seq[LocationTrends] = when(trends(1)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/trends/place.json"
        request.uri.queryString() === Some("id=1")
      }.respondWith("/twitter/rest/trends/trends.json").await
      result === loadJsonAs[Seq[LocationTrends]]("/fixtures/rest/trends/trends.json")
    }

    "get trends for a location without hashtags" in new TwitterTrendClientSpecContext {
      val result: Seq[LocationTrends] = when(trends(1, true)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/trends/place.json"
        request.uri.queryString() === Some("exclude=hashtags&id=1")
      }.respondWith("/twitter/rest/trends/trends.json").await
      result === loadJsonAs[Seq[LocationTrends]]("/fixtures/rest/trends/trends.json")
    }

    "get locations with available trends" in new TwitterTrendClientSpecContext {
      val result: Seq[Location] = when(locationTrends).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/trends/available.json"
      }.respondWith("/twitter/rest/trends/available_locations.json").await
      result === loadJsonAs[Seq[Location]]("/fixtures/rest/trends/available_locations.json")
    }

    "get closest location trends" in new TwitterTrendClientSpecContext {
      val result: Seq[Location] = when(closestLocationTrends(37.781157, -122.400612831116)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/trends/closest.json"
      }.respondWith("/twitter/rest/trends/closest_locations.json").await
      result === loadJsonAs[Seq[Location]]("/fixtures/rest/trends/closest_locations.json")
    }

  }

}

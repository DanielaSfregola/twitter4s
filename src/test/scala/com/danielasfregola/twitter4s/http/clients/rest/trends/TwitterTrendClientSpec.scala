package com.danielasfregola.twitter4s.http.clients.rest.trends

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{Location, LocationTrends, RatedData}
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterTrendClientSpec extends ClientSpec {

  class TwitterTrendClientSpecContext extends RestClientSpecContext with TwitterTrendClient

  "Twitter Trend Client" should {

    "get global trends" in new TwitterTrendClientSpecContext {
      val result: RatedData[Seq[LocationTrends]] = when(globalTrends())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/trends/place.json"
          request.uri.rawQueryString === Some("id=1")
        }
        .respondWithRated("/twitter/rest/trends/trends.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[LocationTrends]]("/fixtures/rest/trends/trends.json")
    }

    "get trends for a location" in new TwitterTrendClientSpecContext {
      val result: RatedData[Seq[LocationTrends]] = when(trends(1))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/trends/place.json"
          request.uri.rawQueryString === Some("id=1")
        }
        .respondWithRated("/twitter/rest/trends/trends.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[LocationTrends]]("/fixtures/rest/trends/trends.json")
    }

    "get trends for a location without hashtags" in new TwitterTrendClientSpecContext {
      val result: RatedData[Seq[LocationTrends]] = when(trends(1, true))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/trends/place.json"
          request.uri.rawQueryString === Some("exclude=hashtags&id=1")
        }
        .respondWithRated("/twitter/rest/trends/trends.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[LocationTrends]]("/fixtures/rest/trends/trends.json")
    }

    "get locations with available trends" in new TwitterTrendClientSpecContext {
      val result: RatedData[Seq[Location]] = when(locationTrends)
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/trends/available.json"
        }
        .respondWithRated("/twitter/rest/trends/available_locations.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[Location]]("/fixtures/rest/trends/available_locations.json")
    }

    "get closest location trends" in new TwitterTrendClientSpecContext {
      val result: RatedData[Seq[Location]] = when(closestLocationTrends(37.781157, -122.400612831116))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/trends/closest.json"
        }
        .respondWithRated("/twitter/rest/trends/closest_locations.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[Location]]("/fixtures/rest/trends/closest_locations.json")
    }

  }

}

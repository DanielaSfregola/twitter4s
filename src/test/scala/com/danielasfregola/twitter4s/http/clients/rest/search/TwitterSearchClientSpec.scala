package com.danielasfregola.twitter4s.http.clients.rest.search

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{RatedData, StatusSearch}
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterSearchClientSpec extends ClientSpec {

  class TwitterSearchClientSpecContext extends RestClientSpecContext with TwitterSearchClient

  "Twitter Search Client" should {

    "search for tweets" in new TwitterSearchClientSpecContext {
      val result: RatedData[StatusSearch] = when(searchTweet("#scala"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/search/tweets.json"
          request.uri.queryString() === Some("count=15&include_entities=true&q=%23scala&result_type=mixed")
        }
        .respondWithRated("/twitter/rest/search/tweets.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[StatusSearch]("/fixtures/rest/search/tweets.json")
    }
  }

}

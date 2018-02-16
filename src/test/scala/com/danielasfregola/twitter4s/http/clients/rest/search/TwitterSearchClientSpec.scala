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
          request.uri.rawQueryString === Some("count=15&include_entities=true&q=%23scala&result_type=mixed")
        }
        .respondWithRated("/twitter/rest/search/tweets.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[StatusSearch]("/fixtures/rest/search/tweets.json")
    }

    "search for tweets with url in it" in new TwitterSearchClientSpecContext {
      val result: RatedData[StatusSearch] = when(searchTweet("https://github.com/ReactiveX/RxJava"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/search/tweets.json"
          request.uri.rawQueryString === Some("count=15&include_entities=true" +
            "&q=https%3A%2F%2Fgithub.com%2FReactiveX%2FRxJava&result_type=mixed")
        }
        .respondWithRated("/twitter/rest/search/tweets.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[StatusSearch]("/fixtures/rest/search/tweets.json")
    }

    "search for tweets with * in it" in new TwitterSearchClientSpecContext {
      val result: RatedData[StatusSearch] = when(searchTweet("Test*Test"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/search/tweets.json"
          request.uri.rawQueryString === Some("count=15&include_entities=true" +
            "&q=Test%2ATest&result_type=mixed")
        }
        .respondWithRated("/twitter/rest/search/tweets.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[StatusSearch]("/fixtures/rest/search/tweets.json")
    }

  }

}

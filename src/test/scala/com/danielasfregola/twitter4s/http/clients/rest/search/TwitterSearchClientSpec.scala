package com.danielasfregola.twitter4s.http.clients.rest.search

import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.StatusSearch

class TwitterSearchClientSpec extends ClientSpec {

  trait TwitterSearchClientSpecContext extends ClientSpecContext with TwitterSearchClient

  "Twitter Search Client" should {

    "search for tweets" in new TwitterSearchClientSpecContext {
      val result: StatusSearch = when(searchTweet("#scala")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/search/tweets.json"
        request.uri.query === Query("count=15&include_entities=true&q=%23scala&result_type=mixed")
      }.respondWith("/twitter/search/tweets.json").await
      result === loadJsonAs[StatusSearch]("/fixtures/search/tweets.json")
    }
  }

}

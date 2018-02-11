package com.danielasfregola.twitter4s.http.clients.rest.savedsearches

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{RatedData, SavedSearch}
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterSavedSearchClientSpec extends ClientSpec {

  class TwitterSavedSearchClientSpecContext extends RestClientSpecContext with TwitterSavedSearchClient

  "Twitter Saved Search Client" should {

    "get saved searches" in new TwitterSavedSearchClientSpecContext {
      val result: RatedData[Seq[SavedSearch]] = when(savedSearches)
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/saved_searches/list.json"
        }
        .respondWithRated("/twitter/rest/savedsearches/list.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[SavedSearch]]("/fixtures/rest/savedsearches/list.json")
    }

    "save a search" in new TwitterSavedSearchClientSpecContext {
      val result: SavedSearch = when(saveSearch("#scala"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/saved_searches/create.json"
          request.uri.rawQueryString === Some("query=%23scala")
        }
        .respondWith("/twitter/rest/savedsearches/create.json")
        .await
      result === loadJsonAs[SavedSearch]("/fixtures/rest/savedsearches/create.json")
    }

    "delete a saved search" in new TwitterSavedSearchClientSpecContext {
      val id = 9569704
      val result: SavedSearch = when(deleteSavedSearch(id))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === s"https://api.twitter.com/1.1/saved_searches/destroy/$id.json"
        }
        .respondWith("/twitter/rest/savedsearches/destroy.json")
        .await
      result === loadJsonAs[SavedSearch]("/fixtures/rest/savedsearches/destroy.json")
    }

    "get saved search by id" in new TwitterSavedSearchClientSpecContext {
      val id = 9569704
      val result: RatedData[SavedSearch] = when(savedSearch(id))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/1.1/saved_searches/show/$id.json"
        }
        .respondWithRated("/twitter/rest/savedsearches/show.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[SavedSearch]("/fixtures/rest/savedsearches/show.json")
    }
  }

}

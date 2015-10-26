package twitter4s.http.clients.savedsearches

import spray.http.HttpMethods
import twitter4s.entities.SavedSearch
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterSavedSearchClientSpec extends ClientSpec {

  trait TwitterSavedSearchClientSpecContext extends ClientSpecContext with TwitterSavedSearchClient

  "Twitter Saved Search Client" should {

    "get saved searches" in new TwitterSavedSearchClientSpecContext {
      val result: Seq[SavedSearch] = when(savedSearch).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/saved_searches/list.json"
      }.respondWith("/twitter/savedsearches/list.json").await
      result === loadJsonAs[Seq[SavedSearch]]("/fixtures/savedsearches/list.json")
    }
  }

}

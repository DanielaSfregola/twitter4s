package com.danielasfregola.twitter4s.http.clients.rest.suggestions

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{Category, RatedData, Suggestions, User}
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterSuggestionClientSpec extends ClientSpec {

  class TwitterSuggestionClientSpecContext extends RestClientSpecContext with TwitterSuggestionClient

  "Twitter Suggestion Client" should {

    "get suggestions of a category" in new TwitterSuggestionClientSpecContext {
      val slug = "twitter"
      val result: RatedData[Suggestions] = when(suggestions(slug))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/1.1/users/suggestions/$slug.json"
          request.uri.rawQueryString === Some("lang=en")
        }
        .respondWithRated("/twitter/rest/suggestions/slug_suggestions.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Suggestions]("/fixtures/rest/suggestions/slug_suggestions.json")
    }

    "get suggested categories" in new TwitterSuggestionClientSpecContext {
      val result: RatedData[Seq[Category]] = when(suggestedCategories())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/users/suggestions.json"
          request.uri.rawQueryString === Some("lang=en")
        }
        .respondWithRated("/twitter/rest/suggestions/categories.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[Category]]("/fixtures/rest/suggestions/categories.json")
    }

    "get suggestions members" in new TwitterSuggestionClientSpecContext {
      val slug = "twitter"
      val result: RatedData[Seq[User]] = when(suggestionsMembers(slug))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === s"https://api.twitter.com/1.1/users/suggestions/$slug/members.json"
        }
        .respondWithRated("/twitter/rest/suggestions/suggestions_members.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[User]]("/fixtures/rest/suggestions/suggestions_members.json")
    }
  }

}

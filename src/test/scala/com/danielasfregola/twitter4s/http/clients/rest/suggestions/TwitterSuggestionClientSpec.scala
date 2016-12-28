package com.danielasfregola.twitter4s.http.clients.rest.suggestions

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{Category, Suggestions, User}
import com.danielasfregola.twitter4s.util.ClientSpec

class TwitterSuggestionClientSpec extends ClientSpec {

  class TwitterSuggestionClientSpecContext extends ClientSpecContext with TwitterSuggestionClient

  "Twitter Suggestion Client" should {

    "get suggestions of a category" in new TwitterSuggestionClientSpecContext {
      val slug = "twitter"
      val result: Suggestions = when(suggestions(slug)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/users/suggestions/$slug.json"
        request.uri.queryString() === Some("lang=en")
      }.respondWith("/twitter/rest/suggestions/slug_suggestions.json").await
      result === loadJsonAs[Suggestions]("/fixtures/rest/suggestions/slug_suggestions.json")
    }

    "get suggested categories" in new TwitterSuggestionClientSpecContext {
      val result: Seq[Category] = when(suggestedCategories()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/suggestions.json"
        request.uri.queryString() === Some("lang=en")
      }.respondWith("/twitter/rest/suggestions/categories.json").await
      result === loadJsonAs[Seq[Category]]("/fixtures/rest/suggestions/categories.json")
    }

    "get suggestions members" in new TwitterSuggestionClientSpecContext {
      val slug = "twitter"
      val result: Seq[User] = when(suggestionsMembers(slug)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/users/suggestions/$slug/members.json"
      }.respondWith("/twitter/rest/suggestions/suggestions_members.json").await
      result === loadJsonAs[Seq[User]]("/fixtures/rest/suggestions/suggestions_members.json")
    }
  }

}

package com.danielasfregola.twitter4s.http.clients.rest.suggestions

import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.{User, Category, Suggestions}

class TwitterSuggestionClientSpec extends ClientSpec {

  class TwitterSuggestionClientSpecContext extends ClientSpecContext with TwitterSuggestionClient

  "Twitter Suggestion Client" should {

    "get suggestions of a category" in new TwitterSuggestionClientSpecContext {
      val slug = "twitter"
      val result: Suggestions = when(getSuggestions(slug)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/users/suggestions/$slug.json"
        request.uri.query === Query("lang=en")
      }.respondWith("/twitter/suggestions/slug_suggestions.json").await
      result === loadJsonAs[Suggestions]("/fixtures/suggestions/slug_suggestions.json")
    }

    "get suggested categories" in new TwitterSuggestionClientSpecContext {
      val result: Seq[Category] = when(getSuggestedCategories()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/suggestions.json"
        request.uri.query === Query("lang=en")
      }.respondWith("/twitter/suggestions/categories.json").await
      result === loadJsonAs[Seq[Category]]("/fixtures/suggestions/categories.json")
    }

    "get suggestions members" in new TwitterSuggestionClientSpecContext {
      val slug = "twitter"
      val result: Seq[User] = when(getSuggestionsMembers(slug)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/users/suggestions/$slug/members.json"
      }.respondWith("/twitter/suggestions/suggestions_members.json").await
      result === loadJsonAs[Seq[User]]("/fixtures/suggestions/suggestions_members.json")
    }
  }

}

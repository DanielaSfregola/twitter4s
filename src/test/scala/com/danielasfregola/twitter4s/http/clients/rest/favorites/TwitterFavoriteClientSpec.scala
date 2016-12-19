package com.danielasfregola.twitter4s.http.clients.rest.favorites


import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.Tweet

class TwitterFavoriteClientSpec extends ClientSpec {

  class TwitterFavoriteClientSpecContext extends ClientSpecContext with TwitterFavoriteClient

  "Twitter Favorite Client" should {

    "get favorites" in new TwitterFavoriteClientSpecContext {
      val result: Seq[Tweet] = when(favoriteStatusesForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/favorites/list.json"
        request.uri.query === Query("count=20&include_entities=true&screen_name=DanielaSfregola")
      }.respondWith("/twitter/rest/favorites/favorites.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/rest/favorites/favorites.json")
    }

    "get favorites per user id" in new TwitterFavoriteClientSpecContext {
      val result: Seq[Tweet] = when(favoriteStatusesForUserId(19018614)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/favorites/list.json"
        request.uri.query === Query("count=20&include_entities=true&user_id=19018614")
      }.respondWith("/twitter/rest/favorites/favorites.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/rest/favorites/favorites.json")
    }

    "favorite a tweet" in new TwitterFavoriteClientSpecContext {
      val result: Tweet = when(favoriteStatus(243138128959913986L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/favorites/create.json"
        request.uri.query === Query("id=243138128959913986&include_entities=true")
      }.respondWith("/twitter/rest/favorites/favorite.json").await
      result === loadJsonAs[Tweet]("/fixtures/rest/favorites/favorite.json")
    }

    "unfavorite a tweet" in new TwitterFavoriteClientSpecContext {
      val result: Tweet = when(unfavoriteStatus(243138128959913986L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/favorites/destroy.json"
        request.uri.query === Query("id=243138128959913986&include_entities=true")
      }.respondWith("/twitter/rest/favorites/unfavorite.json").await
      result === loadJsonAs[Tweet]("/fixtures/rest/favorites/unfavorite.json")
    }
  }

}

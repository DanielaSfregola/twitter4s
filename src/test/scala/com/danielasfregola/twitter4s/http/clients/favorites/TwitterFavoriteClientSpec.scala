package com.danielasfregola.twitter4s.http.clients.favorites


import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.Status

class TwitterFavoriteClientSpec extends ClientSpec {

  trait TwitterFavoriteClientSpecContext extends ClientSpecContext with TwitterFavoriteClient

  "Twitter Favorite Client" should {

    "get favorites" in new TwitterFavoriteClientSpecContext {
      val result: Seq[Status] = when(getFavoriteStatusesForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/favorites/list.json"
        request.uri.query === Query("count=20&include_entities=true&screen_name=DanielaSfregola")
      }.respondWith("/twitter/favorites/favorites.json").await
      result === loadJsonAs[Seq[Status]]("/fixtures/favorites/favorites.json")
    }

    "get favorites per user id" in new TwitterFavoriteClientSpecContext {
      val result: Seq[Status] = when(getFavoriteStatusesForUserId(19018614)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/favorites/list.json"
        request.uri.query === Query("count=20&include_entities=true&user_id=19018614")
      }.respondWith("/twitter/favorites/favorites.json").await
      result === loadJsonAs[Seq[Status]]("/fixtures/favorites/favorites.json")
    }

    "favorite a tweet" in new TwitterFavoriteClientSpecContext {
      val result: Status = when(favoriteStatus(243138128959913986L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/favorites/create.json"
        request.uri.query === Query("id=243138128959913986&include_entities=true")
      }.respondWith("/twitter/favorites/favorite.json").await
      result === loadJsonAs[Status]("/fixtures/favorites/favorite.json")
    }

    "unfavorite a tweet" in new TwitterFavoriteClientSpecContext {
      val result: Status = when(unfavoriteStatus(243138128959913986L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/favorites/destroy.json"
        request.uri.query === Query("id=243138128959913986&include_entities=true")
      }.respondWith("/twitter/favorites/unfavorite.json").await
      result === loadJsonAs[Status]("/fixtures/favorites/unfavorite.json")
    }
  }

}

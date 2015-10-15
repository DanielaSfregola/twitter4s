package twitter4s.http.clients.favorites

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities.Tweet
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterFavoriteClientSpec extends ClientSpec {

  trait TwitterFavoriteClientSpecContext extends ClientSpecContext with TwitterFavoriteClient

  "Twitter Favorite Client" should {

    "get favorites" in new TwitterFavoriteClientSpecContext {
      val result: Seq[Tweet] = when(favorites("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/favorites/list.json"
        request.uri.query === Query("count=20&include_entities=true&screen_name=DanielaSfregola")
      }.respondWith("/twitter/favorites/favorites.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/favorites/favorites.json")
    }

    "get favorites per user id" in new TwitterFavoriteClientSpecContext {
      val result: Seq[Tweet] = when(favoritesForUserId(19018614)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/favorites/list.json"
        request.uri.query === Query("count=20&include_entities=true&user_id=19018614")
      }.respondWith("/twitter/favorites/favorites.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/favorites/favorites.json")
    }

    "favorite a tweet" in new TwitterFavoriteClientSpecContext {
      val result: Tweet = when(favorite(243138128959913986L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/favorites/create.json"
        request.uri.query === Query("id=243138128959913986&include_entities=true")
      }.respondWith("/twitter/favorites/favorite.json").await
      result === loadJsonAs[Tweet]("/fixtures/favorites/favorite.json")
    }

    "unfavorite a tweet" in new TwitterFavoriteClientSpecContext {
      val result: Tweet = when(unfavorite(243138128959913986L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/favorites/destroy.json"
        request.uri.query === Query("id=243138128959913986&include_entities=true")
      }.respondWith("/twitter/favorites/unfavorite.json").await
      result === loadJsonAs[Tweet]("/fixtures/favorites/unfavorite.json")
    }
  }

}

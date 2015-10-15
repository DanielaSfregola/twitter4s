package twitter4s.http.clients.followers

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities.{Users, UserIds, UserIdsStringified}
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterFollowerClientSpec extends ClientSpec {

  trait TwitterFollowerClientSpecContext extends ClientSpecContext with TwitterFollowerClient

  "Twitter Follower Client" should {

    "get followers ids of a specific user by id" in new TwitterFollowerClientSpecContext {
      val result: UserIds = when(followersIdsForUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&stringify_ids=false&user_id=2911461333")
      }.respondWith("/twitter/followers/followers_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/followers/followers_ids.json")
    }

    "get followers ids of a specific user by name" in new TwitterFollowerClientSpecContext {
      val result: UserIds = when(followersIds("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&screen_name=DanielaSfregola&stringify_ids=false")
      }.respondWith("/twitter/followers/followers_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/followers/followers_ids.json")
    }


    "get followers stringified ids of a specific user by id" in new TwitterFollowerClientSpecContext {
      val result: UserIdsStringified = when(followersIdsForUserIdStringified(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&stringify_ids=true&user_id=2911461333")
      }.respondWith("/twitter/followers/followers_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/followers/followers_ids_stringified.json")
    }

    "get followers stringified ids of a specific user by name" in new TwitterFollowerClientSpecContext {
      val result: UserIdsStringified = when(followersIdsStringified("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&screen_name=DanielaSfregola&stringify_ids=true")
      }.respondWith("/twitter/followers/followers_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/followers/followers_ids_stringified.json")
    }

    "get followers of a specific user" in new TwitterFollowerClientSpecContext {
      val result: Users = when(followers("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/list.json"
        request.uri.query === Query("count=-1&cursor=-1&include_user_entities=true&screen_name=DanielaSfregola&skip_status=false")
      }.respondWith("/twitter/followers/followers.json").await
      result === loadJsonAs[Users]("/fixtures/followers/followers.json")
    }

    "get followers of a specific user by user id" in new TwitterFollowerClientSpecContext {
      val result: Users = when(followersForUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/list.json"
        request.uri.query === Query("count=-1&cursor=-1&include_user_entities=true&skip_status=false&user_id=2911461333")
      }.respondWith("/twitter/followers/followers.json").await
      result === loadJsonAs[Users]("/fixtures/followers/followers.json")
    }
  }
}

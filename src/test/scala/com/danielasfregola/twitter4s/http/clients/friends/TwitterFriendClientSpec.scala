package com.danielasfregola.twitter4s.http.clients.friends


import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.{UserIds, UserStringifiedIds, Users}

class TwitterFriendClientSpec extends ClientSpec {

  trait TwitterFriendClientSpecContext extends ClientSpecContext with TwitterFriendClient

  "Twitter Friend Client" should {

    "get friends ids of a specific user by id" in new TwitterFriendClientSpecContext {
      val result: UserIds = when(friendsIdsForUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&stringify_ids=false&user_id=2911461333")
      }.respondWith("/twitter/friends/friends_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/friends/friends_ids.json")
    }

    "get friends ids of a specific user by name" in new TwitterFriendClientSpecContext {
      val result: UserIds = when(friendsIds("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&screen_name=DanielaSfregola&stringify_ids=false")
      }.respondWith("/twitter/friends/friends_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/friends/friends_ids.json")
    }

    "get friends stringified ids of a specific user by id" in new TwitterFriendClientSpecContext {
      val result: UserStringifiedIds = when(friendsForUserIdStringified(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&stringify_ids=true&user_id=2911461333")
      }.respondWith("/twitter/friends/friends_ids_stringified.json").await
      result === loadJsonAs[UserStringifiedIds]("/fixtures/friends/friends_ids_stringified.json")
    }

    "get friends stringified ids of a specific user by name" in new TwitterFriendClientSpecContext {
      val result: UserStringifiedIds = when(friendsStringified("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.query === Query("count=-1&cursor=-1&screen_name=DanielaSfregola&stringify_ids=true")
      }.respondWith("/twitter/friends/friends_ids_stringified.json").await
      result === loadJsonAs[UserStringifiedIds]("/fixtures/friends/friends_ids_stringified.json")
    }

    "get friends of a user" in new TwitterFriendClientSpecContext {
      val result: Users = when(friends("DanielaSfregola", count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/list.json"
        request.uri.query === Query("count=10&cursor=-1&include_user_entities=true&screen_name=DanielaSfregola&skip_status=false")
      }.respondWith("/twitter/friends/users.json").await
      result === loadJsonAs[Users]("/fixtures/friends/users.json")
    }

    "get friends of a user by user id" in new TwitterFriendClientSpecContext {
      val result: Users = when(friendsForUserId(2911461333L, count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/list.json"
        request.uri.query === Query("count=10&cursor=-1&include_user_entities=true&skip_status=false&user_id=2911461333")
      }.respondWith("/twitter/friends/users.json").await
      result === loadJsonAs[Users]("/fixtures/friends/users.json")
    }

  }

}

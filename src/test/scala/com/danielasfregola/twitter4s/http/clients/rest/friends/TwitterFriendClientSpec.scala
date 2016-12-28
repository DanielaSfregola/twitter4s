package com.danielasfregola.twitter4s.http.clients.rest.friends


import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{UserIds, UserStringifiedIds, Users}
import com.danielasfregola.twitter4s.util.ClientSpec

class TwitterFriendClientSpec extends ClientSpec {

  class TwitterFriendClientSpecContext extends ClientSpecContext with TwitterFriendClient

  "Twitter Friend Client" should {

    "get friends ids of a specific user by id" in new TwitterFriendClientSpecContext {
      val result: UserIds = when(friendIdsForUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.queryString() === Some("count=5000&cursor=-1&stringify_ids=false&user_id=2911461333")
      }.respondWith("/twitter/rest/friends/friends_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/rest/friends/friends_ids.json")
    }

    "get friends ids of a specific user by name" in new TwitterFriendClientSpecContext {
      val result: UserIds = when(friendIdsForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.queryString() === Some("count=5000&cursor=-1&screen_name=DanielaSfregola&stringify_ids=false")
      }.respondWith("/twitter/rest/friends/friends_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/rest/friends/friends_ids.json")
    }

    "get friends stringified ids of a specific user by id" in new TwitterFriendClientSpecContext {
      val result: UserStringifiedIds = when(friendStringifiedIdsForUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.queryString() === Some("count=5000&cursor=-1&stringify_ids=true&user_id=2911461333")
      }.respondWith("/twitter/rest/friends/friends_ids_stringified.json").await
      result === loadJsonAs[UserStringifiedIds]("/fixtures/rest/friends/friends_ids_stringified.json")
    }

    "get friends stringified ids of a specific user by name" in new TwitterFriendClientSpecContext {
      val result: UserStringifiedIds = when(friendStringifiedIdsForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
        request.uri.queryString() === Some("count=5000&cursor=-1&screen_name=DanielaSfregola&stringify_ids=true")
      }.respondWith("/twitter/rest/friends/friends_ids_stringified.json").await
      result === loadJsonAs[UserStringifiedIds]("/fixtures/rest/friends/friends_ids_stringified.json")
    }

    "get friends of a user" in new TwitterFriendClientSpecContext {
      val result: Users = when(friendsForUser("DanielaSfregola", count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/list.json"
        request.uri.queryString() === Some("count=10&cursor=-1&include_user_entities=true&screen_name=DanielaSfregola&skip_status=false")
      }.respondWith("/twitter/rest/friends/users.json").await
      result === loadJsonAs[Users]("/fixtures/rest/friends/users.json")
    }

    "get friends of a user by user id" in new TwitterFriendClientSpecContext {
      val result: Users = when(friendsForUserId(2911461333L, count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friends/list.json"
        request.uri.queryString() === Some("count=10&cursor=-1&include_user_entities=true&skip_status=false&user_id=2911461333")
      }.respondWith("/twitter/rest/friends/users.json").await
      result === loadJsonAs[Users]("/fixtures/rest/friends/users.json")
    }

  }

}

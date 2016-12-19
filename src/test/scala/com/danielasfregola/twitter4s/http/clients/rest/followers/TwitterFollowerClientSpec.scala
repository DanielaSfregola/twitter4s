package com.danielasfregola.twitter4s.http.clients.rest.followers


import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.{Users, UserIds, UserStringifiedIds}

class TwitterFollowerClientSpec extends ClientSpec {

  class TwitterFollowerClientSpecContext extends ClientSpecContext with TwitterFollowerClient

  "Twitter Follower Client" should {

    "get followers ids of a specific user by id" in new TwitterFollowerClientSpecContext {
      val result: UserIds = when(followerIdsForUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
        request.uri.query === Query("count=5000&cursor=-1&stringify_ids=false&user_id=2911461333")
      }.respondWith("/twitter/rest/followers/followers_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/rest/followers/followers_ids.json")
    }

    "get followers ids of a specific user by name" in new TwitterFollowerClientSpecContext {
      val result: UserIds = when(followerIdsForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
        request.uri.query === Query("count=5000&cursor=-1&screen_name=DanielaSfregola&stringify_ids=false")
      }.respondWith("/twitter/rest/followers/followers_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/rest/followers/followers_ids.json")
    }


    "get followers stringified ids of a specific user by id" in new TwitterFollowerClientSpecContext {
      val result: UserStringifiedIds = when(followerStringifiedIdsForUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
        request.uri.query === Query("count=5000&cursor=-1&stringify_ids=true&user_id=2911461333")
      }.respondWith("/twitter/rest/followers/followers_ids_stringified.json").await
      result === loadJsonAs[UserStringifiedIds]("/fixtures/rest/followers/followers_ids_stringified.json")
    }

    "get followers stringified ids of a specific user by name" in new TwitterFollowerClientSpecContext {
      val result: UserStringifiedIds = when(followersStringifiedIdsForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
        request.uri.query === Query("count=5000&cursor=-1&screen_name=DanielaSfregola&stringify_ids=true")
      }.respondWith("/twitter/rest/followers/followers_ids_stringified.json").await
      result === loadJsonAs[UserStringifiedIds]("/fixtures/rest/followers/followers_ids_stringified.json")
    }

    "get followers of a specific user" in new TwitterFollowerClientSpecContext {
      val result: Users = when(followersForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/list.json"
        request.uri.query === Query("count=20&cursor=-1&include_user_entities=true&screen_name=DanielaSfregola&skip_status=false")
      }.respondWith("/twitter/rest/followers/followers.json").await
      result === loadJsonAs[Users]("/fixtures/rest/followers/followers.json")
    }

    "get followers of a specific user by user id" in new TwitterFollowerClientSpecContext {
      val result: Users = when(followersForUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/followers/list.json"
        request.uri.query === Query("count=20&cursor=-1&include_user_entities=true&skip_status=false&user_id=2911461333")
      }.respondWith("/twitter/rest/followers/followers.json").await
      result === loadJsonAs[Users]("/fixtures/rest/followers/followers.json")
    }
  }
}

package com.danielasfregola.twitter4s.http.clients.blocks


import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.{UserStringifiedIds, UserIds, User, Users}

class TwitterBlockClientSpec extends ClientSpec {

  trait TwitterBlockClientSpecContext extends ClientSpecContext with TwitterBlockClient

  "Twitter Block Client" should {

    "get blocked users" in new TwitterBlockClientSpecContext {
      val result: Users = when(getBlockedUsers()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/list.json"
      }.respondWith("/twitter/blocks/blocked_users.json").await
      result === loadJsonAs[Users]("/fixtures/blocks/blocked_users.json")
    }

    "get blocked user ids" in new TwitterBlockClientSpecContext {
      val result: UserIds = when(getBlockedUserIds()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/ids.json"
        request.uri.query === Query("cursor=-1&stringify_ids=false")
      }.respondWith("/twitter/blocks/ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/blocks/ids.json")
    }

    "get blocked user stringified ids" in new TwitterBlockClientSpecContext {
      val result: UserStringifiedIds = when(getBlockedUserStringifiedIds()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/ids.json"
        request.uri.query === Query("cursor=-1&stringify_ids=true")
      }.respondWith("/twitter/blocks/stringified_ids.json").await
      result === loadJsonAs[UserStringifiedIds]("/fixtures/blocks/stringified_ids.json")
    }

    "block user"  in new TwitterBlockClientSpecContext {
      val result: User = when(blockUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/create.json"
        request.uri.query === Query("include_entities=true&screen_name=marcobonzanini&skip_status=false")
      }.respondWith("/twitter/blocks/user.json").await
      result === loadJsonAs[User]("/fixtures/blocks/user.json")
    }

    "block user by user id"  in new TwitterBlockClientSpecContext {
      val result: User = when(blockUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/create.json"
        request.uri.query === Query("include_entities=true&skip_status=false&user_id=19018614")
      }.respondWith("/twitter/blocks/user.json").await
      result === loadJsonAs[User]("/fixtures/blocks/user.json")
    }

    "unblock user"  in new TwitterBlockClientSpecContext {
      val result: User = when(unblockUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/destroy.json"
        request.uri.query === Query("include_entities=true&screen_name=marcobonzanini&skip_status=false")
      }.respondWith("/twitter/blocks/user.json").await
      result === loadJsonAs[User]("/fixtures/blocks/user.json")
    }

    "unblock user by user id"  in new TwitterBlockClientSpecContext {
      val result: User = when(unblockUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/destroy.json"
        request.uri.query === Query("include_entities=true&skip_status=false&user_id=19018614")
      }.respondWith("/twitter/blocks/user.json").await
      result === loadJsonAs[User]("/fixtures/blocks/user.json")
    }
  }


}

package com.danielasfregola.twitter4s.http.clients.rest.blocks


import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{User, UserIds, UserStringifiedIds, Users}
import com.danielasfregola.twitter4s.util.ClientSpec

class TwitterBlockClientSpec extends ClientSpec {

  class TwitterBlockClientSpecContext extends ClientSpecContext with TwitterBlockClient

  "Twitter Block Client" should {

    "get blocked users" in new TwitterBlockClientSpecContext {
      val result: Users = when(blockedUsers()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/list.json"
      }.respondWith("/twitter/rest/blocks/blocked_users.json").await
      result === loadJsonAs[Users]("/fixtures/rest/blocks/blocked_users.json")
    }

    "get blocked user ids" in new TwitterBlockClientSpecContext {
      val result: UserIds = when(blockedUserIds()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/ids.json"
        request.uri.queryString() === Some("cursor=-1&stringify_ids=false")
      }.respondWith("/twitter/rest/blocks/ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/rest/blocks/ids.json")
    }

    "get blocked user stringified ids" in new TwitterBlockClientSpecContext {
      val result: UserStringifiedIds = when(blockedUserStringifiedIds()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/ids.json"
        request.uri.queryString() === Some("cursor=-1&stringify_ids=true")
      }.respondWith("/twitter/rest/blocks/stringified_ids.json").await
      result === loadJsonAs[UserStringifiedIds]("/fixtures/rest/blocks/stringified_ids.json")
    }

    "block user"  in new TwitterBlockClientSpecContext {
      val result: User = when(blockUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/create.json"
        request.uri.queryString() === Some("include_entities=true&screen_name=marcobonzanini&skip_status=false")
      }.respondWith("/twitter/rest/blocks/user.json").await
      result === loadJsonAs[User]("/fixtures/rest/blocks/user.json")
    }

    "block user by user id"  in new TwitterBlockClientSpecContext {
      val result: User = when(blockUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/create.json"
        request.uri.queryString() === Some("include_entities=true&skip_status=false&user_id=19018614")
      }.respondWith("/twitter/rest/blocks/user.json").await
      result === loadJsonAs[User]("/fixtures/rest/blocks/user.json")
    }

    "unblock user"  in new TwitterBlockClientSpecContext {
      val result: User = when(unblockUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/destroy.json"
        request.uri.queryString() === Some("include_entities=true&screen_name=marcobonzanini&skip_status=false")
      }.respondWith("/twitter/rest/blocks/user.json").await
      result === loadJsonAs[User]("/fixtures/rest/blocks/user.json")
    }

    "unblock user by user id"  in new TwitterBlockClientSpecContext {
      val result: User = when(unblockUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/destroy.json"
        request.uri.queryString() === Some("include_entities=true&skip_status=false&user_id=19018614")
      }.respondWith("/twitter/rest/blocks/user.json").await
      result === loadJsonAs[User]("/fixtures/rest/blocks/user.json")
    }
  }


}

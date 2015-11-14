package com.danielasfregola.twitter4s.http.clients.blocks

import com.danielasfregola.twitter4s.http.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.{User, Users}
import com.danielasfregola.twitter4s.http.ClientSpecContext

class TwitterBlockClientSpec extends ClientSpec {

  trait TwitterBlockClientSpecContext extends ClientSpecContext with TwitterBlockClient

  "Twitter Block Client" should {

    "get blocked users" in new TwitterBlockClientSpecContext {
      val result: Users = when(blockedUsers()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/list.json"
      }.respondWith("/twitter/blocks/blocked_users.json").await
      result === loadJsonAs[Users]("/twitter/blocks/blocked_users.json")
    }

    "block user"  in new TwitterBlockClientSpecContext {
      val result: User = when(block("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/create.json"
        request.uri.query === Query("include_entities=true&screen_name=marcobonzanini&skip_status=false")
      }.respondWith("/twitter/blocks/user.json").await
      result === loadJsonAs[User]("/twitter/blocks/user.json")
    }

    "block user by user id"  in new TwitterBlockClientSpecContext {
      val result: User = when(blockUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/create.json"
        request.uri.query === Query("include_entities=true&skip_status=false&user_id=19018614")
      }.respondWith("/twitter/blocks/user.json").await
      result === loadJsonAs[User]("/twitter/blocks/user.json")
    }

    "unblock user"  in new TwitterBlockClientSpecContext {
      val result: User = when(unblock("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/destroy.json"
        request.uri.query === Query("include_entities=true&screen_name=marcobonzanini&skip_status=false")
      }.respondWith("/twitter/blocks/user.json").await
      result === loadJsonAs[User]("/twitter/blocks/user.json")
    }

    "unblock user by user id"  in new TwitterBlockClientSpecContext {
      val result: User = when(unblockUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/blocks/destroy.json"
        request.uri.query === Query("include_entities=true&skip_status=false&user_id=19018614")
      }.respondWith("/twitter/blocks/user.json").await
      result === loadJsonAs[User]("/twitter/blocks/user.json")
    }
  }


}

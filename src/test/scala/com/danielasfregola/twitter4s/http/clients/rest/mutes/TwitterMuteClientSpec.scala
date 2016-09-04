package com.danielasfregola.twitter4s.http.clients.rest.mutes

import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities.{Users, UserIds, User}

class TwitterMuteClientSpec extends ClientSpec {

  class TwitterMuteClientSpecContext extends ClientSpecContext with TwitterMuteClient

  "Twitter Mute Client" should {

    "mute a user" in new TwitterMuteClientSpecContext {
      val result: User = when(muteUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/mutes/users/create.json"
        request.uri.query === Query("screen_name=marcobonzanini")
      }.respondWith("/twitter/mutes/user.json").await
      result === loadJsonAs[User]("/fixtures/mutes/user.json")
    }

    "mute a user by id" in new TwitterMuteClientSpecContext {
      val result: User = when(muteUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/mutes/users/create.json"
        request.uri.query === Query("user_id=19018614")
      }.respondWith("/twitter/mutes/user.json").await
      result === loadJsonAs[User]("/fixtures/mutes/user.json")
    }

    "unmute a user" in new TwitterMuteClientSpecContext {
      val result: User = when(unmuteUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/mutes/users/destroy.json"
        request.uri.query === Query("screen_name=marcobonzanini")
      }.respondWith("/twitter/mutes/user.json").await
      result === loadJsonAs[User]("/fixtures/mutes/user.json")
    }

    "unmute a user by id" in new TwitterMuteClientSpecContext {
      val result: User = when(unmuteUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/mutes/users/destroy.json"
        request.uri.query === Query("user_id=19018614")
      }.respondWith("/twitter/mutes/user.json").await
      result === loadJsonAs[User]("/fixtures/mutes/user.json")
    }

    "get muted users ids" in new TwitterMuteClientSpecContext {
      val result: UserIds = when(getMutedUserIds()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/mutes/users/ids.json"
        request.uri.query === Query("cursor=-1")
      }.respondWith("/twitter/mutes/muted_users_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/mutes/muted_users_ids.json")
    }

    "get muted users" in new TwitterMuteClientSpecContext {
      val result: Users = when(getMutedUsers()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/mutes/users/list.json"
        request.uri.query === Query("cursor=-1&include_entities=true&skip_status=false")
      }.respondWith("/twitter/mutes/users.json").await
      result === loadJsonAs[Users]("/fixtures/mutes/users.json")
    }
  }
}

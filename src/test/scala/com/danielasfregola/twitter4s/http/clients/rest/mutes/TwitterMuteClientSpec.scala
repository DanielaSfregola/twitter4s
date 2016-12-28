package com.danielasfregola.twitter4s.http.clients.rest.mutes

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{User, UserIds, Users}
import com.danielasfregola.twitter4s.util.ClientSpec

class TwitterMuteClientSpec extends ClientSpec {

  class TwitterMuteClientSpecContext extends ClientSpecContext with TwitterMuteClient

  "Twitter Mute Client" should {

    "mute a user" in new TwitterMuteClientSpecContext {
      val result: User = when(muteUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/mutes/users/create.json"
        request.uri.queryString() === Some("screen_name=marcobonzanini")
      }.respondWith("/twitter/rest/mutes/user.json").await
      result === loadJsonAs[User]("/fixtures/rest/mutes/user.json")
    }

    "mute a user by id" in new TwitterMuteClientSpecContext {
      val result: User = when(muteUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/mutes/users/create.json"
        request.uri.queryString() === Some("user_id=19018614")
      }.respondWith("/twitter/rest/mutes/user.json").await
      result === loadJsonAs[User]("/fixtures/rest/mutes/user.json")
    }

    "unmute a user" in new TwitterMuteClientSpecContext {
      val result: User = when(unmuteUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/mutes/users/destroy.json"
        request.uri.queryString() === Some("screen_name=marcobonzanini")
      }.respondWith("/twitter/rest/mutes/user.json").await
      result === loadJsonAs[User]("/fixtures/rest/mutes/user.json")
    }

    "unmute a user by id" in new TwitterMuteClientSpecContext {
      val result: User = when(unmuteUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/mutes/users/destroy.json"
        request.uri.queryString() === Some("user_id=19018614")
      }.respondWith("/twitter/rest/mutes/user.json").await
      result === loadJsonAs[User]("/fixtures/rest/mutes/user.json")
    }

    "get muted users ids" in new TwitterMuteClientSpecContext {
      val result: UserIds = when(mutedUserIds()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/mutes/users/ids.json"
        request.uri.queryString() === Some("cursor=-1")
      }.respondWith("/twitter/rest/mutes/muted_users_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/rest/mutes/muted_users_ids.json")
    }

    "get muted users" in new TwitterMuteClientSpecContext {
      val result: Users = when(mutedUsers()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/mutes/users/list.json"
        request.uri.queryString() === Some("cursor=-1&include_entities=true&skip_status=false")
      }.respondWith("/twitter/rest/mutes/users.json").await
      result === loadJsonAs[Users]("/fixtures/rest/mutes/users.json")
    }
  }
}

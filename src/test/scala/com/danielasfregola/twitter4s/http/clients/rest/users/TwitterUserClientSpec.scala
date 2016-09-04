package com.danielasfregola.twitter4s.http.clients.rest.users

import com.danielasfregola.twitter4s.entities.{Banners, User}
import com.danielasfregola.twitter4s.util.{ClientSpecContext, ClientSpec}
import spray.http.HttpMethods
import spray.http.Uri.Query

class TwitterUserClientSpec extends ClientSpec {

class TwitterUserClientSpecContext extends ClientSpecContext with TwitterUserClient

  "Twitter User Client" should {

    "retrieve users" in new TwitterUserClientSpecContext {
      val result: Seq[User] = when(getUsers("marcobonzanini", "odersky")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/lookup.json"
        request.uri.query === Query("include_entities=true&screen_name=marcobonzanini,odersky")
      }.respondWith("/twitter/users/users.json").await
      result === loadJsonAs[Seq[User]]("/fixtures/users/users.json")
    }

    "reject request if no screen names have been provided to retreive users" in new TwitterUserClientSpecContext {
      getUsers() must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }

    "retrieve users by user ids" in new TwitterUserClientSpecContext {
      val result: Seq[User] = when(getUsersByIds(19018614, 17765013)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/lookup.json"
        request.uri.query === Query("include_entities=true&user_id=19018614,17765013")
      }.respondWith("/twitter/users/users.json").await
      result === loadJsonAs[Seq[User]]("/fixtures/users/users.json")
    }

    "reject request if no ids have been provided to retreive users by ids" in new TwitterUserClientSpecContext {
      getUsersByIds() must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }

    "retrieve user" in new TwitterUserClientSpecContext {
      val result: User = when(getUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/show.json"
        request.uri.query === Query("include_entities=true&screen_name=marcobonzanini")
      }.respondWith("/twitter/users/user.json").await
      result === loadJsonAs[User]("/fixtures/users/user.json")
    }

    "retrieve user by id" in new TwitterUserClientSpecContext {
      val result: User = when(getUserById(19018614)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/show.json"
        request.uri.query === Query("include_entities=true&user_id=19018614")
      }.respondWith("/twitter/users/user.json").await
      result === loadJsonAs[User]("/fixtures/users/user.json")
    }

    "get the profile banners of a user" in new  TwitterUserClientSpecContext {
      val result: Banners = when(getProfileBannersForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/profile_banner.json"
        request.uri.query === Query("screen_name=DanielaSfregola")
      }.respondWith("/twitter/users/profile_banner.json").await
      result === loadJsonAs[Banners]("/fixtures/users/profile_banner.json")
    }

    "get the profile banners of a user by id" in new  TwitterUserClientSpecContext {
      val result: Banners = when(getProfileBannersForUserId(19018614)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/profile_banner.json"
        request.uri.query === Query("user_id=19018614")
      }.respondWith("/twitter/users/profile_banner.json").await
      result === loadJsonAs[Banners]("/fixtures/users/profile_banner.json")
    }

    "search for a user" in new TwitterUserClientSpecContext {
      val result: Seq[User] = when(searchForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/search.json"
        request.uri.query === Query("count=20&include_entities=true&page=-1&q=DanielaSfregola")
      }.respondWith("/twitter/users/users.json").await
      result === loadJsonAs[Seq[User]]("/fixtures/users/users.json")
    }
  }
}

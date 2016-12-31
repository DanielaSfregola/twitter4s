package com.danielasfregola.twitter4s.http.clients.rest.users

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{Banners, User}
import com.danielasfregola.twitter4s.util.rest.ClientSpec

class TwitterUserClientSpec extends ClientSpec {

class TwitterUserClientSpecContext extends ClientSpecContext with TwitterUserClient

  "Twitter User Client" should {

    "retrieve users" in new TwitterUserClientSpecContext {
      val result: Seq[User] = when(users("marcobonzanini", "odersky")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/lookup.json"
        request.uri.queryString() === Some("include_entities=true&screen_name=marcobonzanini,odersky")
      }.respondWith("/twitter/rest/users/users.json").await
      result === loadJsonAs[Seq[User]]("/fixtures/rest/users/users.json")
    }

    "reject request if no screen names have been provided to retreive users" in new TwitterUserClientSpecContext {
      users() must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }

    "retrieve users by user ids" in new TwitterUserClientSpecContext {
      val result: Seq[User] = when(usersByIds(19018614, 17765013)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/lookup.json"
        request.uri.queryString() === Some("include_entities=true&user_id=19018614,17765013")
      }.respondWith("/twitter/rest/users/users.json").await
      result === loadJsonAs[Seq[User]]("/fixtures/rest/users/users.json")
    }

    "reject request if no ids have been provided to retreive users by ids" in new TwitterUserClientSpecContext {
      usersByIds() must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }

    "retrieve user" in new TwitterUserClientSpecContext {
      val result: User = when(user("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/show.json"
        request.uri.queryString() === Some("include_entities=true&screen_name=marcobonzanini")
      }.respondWith("/twitter/rest/users/user.json").await
      result === loadJsonAs[User]("/fixtures/rest/users/user.json")
    }

    "retrieve user by id" in new TwitterUserClientSpecContext {
      val result: User = when(userById(19018614)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/show.json"
        request.uri.queryString() === Some("include_entities=true&user_id=19018614")
      }.respondWith("/twitter/rest/users/user.json").await
      result === loadJsonAs[User]("/fixtures/rest/users/user.json")
    }

    "get the profile banners of a user" in new  TwitterUserClientSpecContext {
      val result: Banners = when(profileBannersForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/profile_banner.json"
        request.uri.queryString() === Some("screen_name=DanielaSfregola")
      }.respondWith("/twitter/rest/users/profile_banner.json").await
      result === loadJsonAs[Banners]("/fixtures/rest/users/profile_banner.json")
    }

    "get the profile banners of a user by id" in new  TwitterUserClientSpecContext {
      val result: Banners = when(profileBannersForUserId(19018614)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/profile_banner.json"
        request.uri.queryString() === Some("user_id=19018614")
      }.respondWith("/twitter/rest/users/profile_banner.json").await
      result === loadJsonAs[Banners]("/fixtures/rest/users/profile_banner.json")
    }

    "search for a user" in new TwitterUserClientSpecContext {
      val result: Seq[User] = when(searchForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/users/search.json"
        request.uri.queryString() === Some("count=20&include_entities=true&page=-1&q=DanielaSfregola")
      }.respondWith("/twitter/rest/users/users.json").await
      result === loadJsonAs[Seq[User]]("/fixtures/rest/users/users.json")
    }
  }
}

package com.danielasfregola.twitter4s.http.clients.rest.users

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{Banners, RatedData, User}
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterUserClientSpec extends ClientSpec {

  class TwitterUserClientSpecContext extends RestClientSpecContext with TwitterUserClient

  "Twitter User Client" should {

    "retrieve users" in new TwitterUserClientSpecContext {
      val result: RatedData[Seq[User]] = when(users("marcobonzanini", "odersky"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/users/lookup.json"
          request.uri.rawQueryString === Some("include_entities=true&screen_name=marcobonzanini%2Codersky")
        }
        .respondWithRated("/twitter/rest/users/users.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[User]]("/fixtures/rest/users/users.json")
    }

    "reject request if no screen names have been provided to retreive users" in new TwitterUserClientSpecContext {
      users() must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }

    "retrieve users by user ids" in new TwitterUserClientSpecContext {
      val result: RatedData[Seq[User]] = when(usersByIds(19018614, 17765013))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/users/lookup.json"
          request.uri.rawQueryString === Some("include_entities=true&user_id=19018614%2C17765013")
        }
        .respondWithRated("/twitter/rest/users/users.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[User]]("/fixtures/rest/users/users.json")
    }

    "reject request if no ids have been provided to retreive users by ids" in new TwitterUserClientSpecContext {
      usersByIds() must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }

    "retrieve user" in new TwitterUserClientSpecContext {
      val result: RatedData[User] = when(user("marcobonzanini"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/users/show.json"
          request.uri.rawQueryString === Some("include_entities=true&screen_name=marcobonzanini")
        }
        .respondWithRated("/twitter/rest/users/user.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[User]("/fixtures/rest/users/user.json")
    }

    "retrieve user by id" in new TwitterUserClientSpecContext {
      val result: RatedData[User] = when(userById(19018614))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/users/show.json"
          request.uri.rawQueryString === Some("include_entities=true&user_id=19018614")
        }
        .respondWithRated("/twitter/rest/users/user.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[User]("/fixtures/rest/users/user.json")
    }

    "get the profile banners of a user" in new TwitterUserClientSpecContext {
      val result: RatedData[Banners] = when(profileBannersForUser("DanielaSfregola"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/users/profile_banner.json"
          request.uri.rawQueryString === Some("screen_name=DanielaSfregola")
        }
        .respondWithRated("/twitter/rest/users/profile_banner.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Banners]("/fixtures/rest/users/profile_banner.json")
    }

    "get the profile banners of a user by id" in new TwitterUserClientSpecContext {
      val result: RatedData[Banners] = when(profileBannersForUserId(19018614))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/users/profile_banner.json"
          request.uri.rawQueryString === Some("user_id=19018614")
        }
        .respondWithRated("/twitter/rest/users/profile_banner.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Banners]("/fixtures/rest/users/profile_banner.json")
    }

    "search for a user" in new TwitterUserClientSpecContext {
      val result: RatedData[Seq[User]] = when(searchForUser("DanielaSfregola"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/users/search.json"
          request.uri.rawQueryString === Some("count=20&include_entities=true&page=-1&q=DanielaSfregola")
        }
        .respondWithRated("/twitter/rest/users/users.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[User]]("/fixtures/rest/users/users.json")
    }
  }
}

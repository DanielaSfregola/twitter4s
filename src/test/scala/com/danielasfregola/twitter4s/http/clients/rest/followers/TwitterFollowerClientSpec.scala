package com.danielasfregola.twitter4s.http.clients.rest.followers

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{RatedData, UserIds, UserStringifiedIds, Users}
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterFollowerClientSpec extends ClientSpec {

  class TwitterFollowerClientSpecContext extends RestClientSpecContext with TwitterFollowerClient

  "Twitter Follower Client" should {

    "get followers ids of a specific user by id" in new TwitterFollowerClientSpecContext {
      val result: RatedData[UserIds] = when(followerIdsForUserId(2911461333L))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
          request.uri.rawQueryString === Some("count=5000&cursor=-1&stringify_ids=false&user_id=2911461333")
        }
        .respondWithRated("/twitter/rest/followers/followers_ids.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserIds]("/fixtures/rest/followers/followers_ids.json")
    }

    "get followers ids of a specific user by name" in new TwitterFollowerClientSpecContext {
      val result: RatedData[UserIds] = when(followerIdsForUser("DanielaSfregola"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
          request.uri.rawQueryString === Some("count=5000&cursor=-1&screen_name=DanielaSfregola&stringify_ids=false")
        }
        .respondWithRated("/twitter/rest/followers/followers_ids.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserIds]("/fixtures/rest/followers/followers_ids.json")
    }

    "get followers stringified ids of a specific user by id" in new TwitterFollowerClientSpecContext {
      val result: RatedData[UserStringifiedIds] = when(followerStringifiedIdsForUserId(2911461333L))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
          request.uri.rawQueryString === Some("count=5000&cursor=-1&stringify_ids=true&user_id=2911461333")
        }
        .respondWithRated("/twitter/rest/followers/followers_ids_stringified.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserStringifiedIds]("/fixtures/rest/followers/followers_ids_stringified.json")
    }

    "get followers stringified ids of a specific user by name" in new TwitterFollowerClientSpecContext {
      val result: RatedData[UserStringifiedIds] = when(followersStringifiedIdsForUser("DanielaSfregola"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/followers/ids.json"
          request.uri.rawQueryString === Some("count=5000&cursor=-1&screen_name=DanielaSfregola&stringify_ids=true")
        }
        .respondWithRated("/twitter/rest/followers/followers_ids_stringified.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserStringifiedIds]("/fixtures/rest/followers/followers_ids_stringified.json")
    }

    "get followers of a specific user" in new TwitterFollowerClientSpecContext {
      val result: RatedData[Users] = when(followersForUser("DanielaSfregola"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/followers/list.json"
          request.uri.rawQueryString === Some(
            "count=20&cursor=-1&include_user_entities=true&screen_name=DanielaSfregola&skip_status=false")
        }
        .respondWithRated("/twitter/rest/followers/followers.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Users]("/fixtures/rest/followers/followers.json")
    }

    "get followers of a specific user by user id" in new TwitterFollowerClientSpecContext {
      val result: RatedData[Users] = when(followersForUserId(2911461333L))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/followers/list.json"
          request.uri.rawQueryString === Some(
            "count=20&cursor=-1&include_user_entities=true&skip_status=false&user_id=2911461333")
        }
        .respondWithRated("/twitter/rest/followers/followers.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Users]("/fixtures/rest/followers/followers.json")
    }
  }
}

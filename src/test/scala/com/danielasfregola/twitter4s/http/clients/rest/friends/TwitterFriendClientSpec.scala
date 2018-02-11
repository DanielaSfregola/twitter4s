package com.danielasfregola.twitter4s.http.clients.rest.friends

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities.{RatedData, UserIds, UserStringifiedIds, Users}
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterFriendClientSpec extends ClientSpec {

  class TwitterFriendClientSpecContext extends RestClientSpecContext with TwitterFriendClient

  "Twitter Friend Client" should {

    "get friends ids of a specific user by id" in new TwitterFriendClientSpecContext {
      val result: RatedData[UserIds] = when(friendIdsForUserId(2911461333L))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
          request.uri.rawQueryString === Some("count=5000&cursor=-1&stringify_ids=false&user_id=2911461333")
        }
        .respondWithRated("/twitter/rest/friends/friends_ids.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserIds]("/fixtures/rest/friends/friends_ids.json")
    }

    "get friends ids of a specific user by name" in new TwitterFriendClientSpecContext {
      val result: RatedData[UserIds] = when(friendIdsForUser("DanielaSfregola"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
          request.uri.rawQueryString === Some("count=5000&cursor=-1&screen_name=DanielaSfregola&stringify_ids=false")
        }
        .respondWithRated("/twitter/rest/friends/friends_ids.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserIds]("/fixtures/rest/friends/friends_ids.json")
    }

    "get friends stringified ids of a specific user by id" in new TwitterFriendClientSpecContext {
      val result: RatedData[UserStringifiedIds] = when(friendStringifiedIdsForUserId(2911461333L))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
          request.uri.rawQueryString === Some("count=5000&cursor=-1&stringify_ids=true&user_id=2911461333")
        }
        .respondWithRated("/twitter/rest/friends/friends_ids_stringified.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserStringifiedIds]("/fixtures/rest/friends/friends_ids_stringified.json")
    }

    "get friends stringified ids of a specific user by name" in new TwitterFriendClientSpecContext {
      val result: RatedData[UserStringifiedIds] = when(friendStringifiedIdsForUser("DanielaSfregola"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friends/ids.json"
          request.uri.rawQueryString === Some("count=5000&cursor=-1&screen_name=DanielaSfregola&stringify_ids=true")
        }
        .respondWithRated("/twitter/rest/friends/friends_ids_stringified.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserStringifiedIds]("/fixtures/rest/friends/friends_ids_stringified.json")
    }

    "get friends of a user" in new TwitterFriendClientSpecContext {
      val result: RatedData[Users] = when(friendsForUser("DanielaSfregola", count = 10))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friends/list.json"
          request.uri.rawQueryString === Some(
            "count=10&cursor=-1&include_user_entities=true&screen_name=DanielaSfregola&skip_status=false")
        }
        .respondWithRated("/twitter/rest/friends/users.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Users]("/fixtures/rest/friends/users.json")
    }

    "get friends of a user by user id" in new TwitterFriendClientSpecContext {
      val result: RatedData[Users] = when(friendsForUserId(2911461333L, count = 10))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friends/list.json"
          request.uri.rawQueryString === Some(
            "count=10&cursor=-1&include_user_entities=true&skip_status=false&user_id=2911461333")
        }
        .respondWithRated("/twitter/rest/friends/users.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Users]("/fixtures/rest/friends/users.json")
    }

  }

}

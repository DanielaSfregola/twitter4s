package com.danielasfregola.twitter4s.http.clients.rest.blocks

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterBlockClientSpec extends ClientSpec {

  class TwitterBlockClientSpecContext extends RestClientSpecContext with TwitterBlockClient

  "Twitter Block Client" should {

    "get blocked users" in new TwitterBlockClientSpecContext {
      val result: RatedData[Users] = when(blockedUsers())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/blocks/list.json"
        }
        .respondWithRated("/twitter/rest/blocks/blocked_users.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Users]("/fixtures/rest/blocks/blocked_users.json")
    }

    "get blocked user ids" in new TwitterBlockClientSpecContext {
      val result: RatedData[UserIds] = when(blockedUserIds())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/blocks/ids.json"
          request.uri.rawQueryString === Some("cursor=-1&stringify_ids=false")
        }
        .respondWithRated("/twitter/rest/blocks/ids.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserIds]("/fixtures/rest/blocks/ids.json")
    }

    "get blocked user stringified ids" in new TwitterBlockClientSpecContext {
      val result: RatedData[UserStringifiedIds] = when(blockedUserStringifiedIds())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/blocks/ids.json"
          request.uri.rawQueryString === Some("cursor=-1&stringify_ids=true")
        }
        .respondWithRated("/twitter/rest/blocks/stringified_ids.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserStringifiedIds]("/fixtures/rest/blocks/stringified_ids.json")
    }

    "block user" in new TwitterBlockClientSpecContext {
      val result: User = when(blockUser("marcobonzanini"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/blocks/create.json"
          request.uri.rawQueryString === Some("include_entities=true&screen_name=marcobonzanini&skip_status=false")
        }
        .respondWith("/twitter/rest/blocks/user.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/blocks/user.json")
    }

    "block user by user id" in new TwitterBlockClientSpecContext {
      val result: User = when(blockUserId(19018614L))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/blocks/create.json"
          request.uri.rawQueryString === Some("include_entities=true&skip_status=false&user_id=19018614")
        }
        .respondWith("/twitter/rest/blocks/user.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/blocks/user.json")
    }

    "unblock user" in new TwitterBlockClientSpecContext {
      val result: User = when(unblockUser("marcobonzanini"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/blocks/destroy.json"
          request.uri.rawQueryString === Some("include_entities=true&screen_name=marcobonzanini&skip_status=false")
        }
        .respondWith("/twitter/rest/blocks/user.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/blocks/user.json")
    }

    "unblock user by user id" in new TwitterBlockClientSpecContext {
      val result: User = when(unblockUserId(19018614L))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/blocks/destroy.json"
          request.uri.rawQueryString === Some("include_entities=true&skip_status=false&user_id=19018614")
        }
        .respondWith("/twitter/rest/blocks/user.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/blocks/user.json")
    }
  }

}

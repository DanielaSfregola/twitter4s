package com.danielasfregola.twitter4s.http.clients.rest.friendships

import akka.http.scaladsl.model.HttpMethods
import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.helpers.ClientSpec

class TwitterFriendshipClientSpec extends ClientSpec {

  class TwitterFriendshipClientSpecContext extends RestClientSpecContext with TwitterFriendshipClient

  "Twitter Friendship Client" should {

    "get all blocked users" in new TwitterFriendshipClientSpecContext {
      val result: RatedData[Seq[Long]] = when(noRetweetsUserIds)
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/no_retweets/ids.json"
          request.uri.rawQueryString === Some("stringify_ids=false")
        }
        .respondWithRated("/twitter/rest/friendships/blocked_users.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[Long]]("/fixtures/rest/friendships/blocked_users.json")
    }

    "get all blocked users stringified" in new TwitterFriendshipClientSpecContext {
      val result: RatedData[Seq[String]] = when(noRetweetsUserStringifiedIds)
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/no_retweets/ids.json"
          request.uri.rawQueryString === Some("stringify_ids=true")
        }
        .respondWithRated("/twitter/rest/friendships/blocked_users_stringified.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[String]]("/fixtures/rest/friendships/blocked_users_stringified.json")
    }

    "get incoming friendships" in new TwitterFriendshipClientSpecContext {
      val result: RatedData[UserIds] = when(incomingFriendshipIds())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/incoming.json"
          request.uri.rawQueryString === Some("cursor=-1&stringify_ids=false")
        }
        .respondWithRated("/twitter/rest/friendships/incoming_friendships_ids.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserIds]("/fixtures/rest/friendships/incoming_friendships_ids.json")
    }

    "get incoming friendships stringified" in new TwitterFriendshipClientSpecContext {
      val result: RatedData[UserStringifiedIds] = when(incomingFriendshipStringifiedIds())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/incoming.json"
          request.uri.rawQueryString === Some("cursor=-1&stringify_ids=true")
        }
        .respondWithRated("/twitter/rest/friendships/incoming_friendships_ids_stringified.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserStringifiedIds](
        "/fixtures/rest/friendships/incoming_friendships_ids_stringified.json")
    }

    "get outgoing friendships" in new TwitterFriendshipClientSpecContext {
      val result: RatedData[UserIds] = when(outgoingFriendshipIds())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/outgoing.json"
          request.uri.rawQueryString === Some("cursor=-1&stringify_ids=false")
        }
        .respondWithRated("/twitter/rest/friendships/outgoing_friendships_ids.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserIds]("/fixtures/rest/friendships/outgoing_friendships_ids.json")
    }

    "get outgoing friendships stringified" in new TwitterFriendshipClientSpecContext {
      val result: RatedData[UserStringifiedIds] = when(outgoingFriendshipStringifiedIds())
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/outgoing.json"
          request.uri.rawQueryString === Some("cursor=-1&stringify_ids=true")
        }
        .respondWithRated("/twitter/rest/friendships/outgoing_friendships_ids_stringified.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[UserStringifiedIds](
        "/fixtures/rest/friendships/outgoing_friendships_ids_stringified.json")
    }

    "follow a user" in new TwitterFriendshipClientSpecContext {
      val result: User = when(followUser("marcobonzanini"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/create.json"
          request.uri.rawQueryString === Some("follow=true&screen_name=marcobonzanini")
        }
        .respondWith("/twitter/rest/friendships/follow.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/friendships/follow.json")
    }

    "follow a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: User = when(followUserId(19018614L))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/create.json"
          request.uri.rawQueryString === Some("follow=true&user_id=19018614")
        }
        .respondWith("/twitter/rest/friendships/follow.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/friendships/follow.json")
    }

    "unfollow a user" in new TwitterFriendshipClientSpecContext {
      val result: User = when(unfollowUser("marcobonzanini"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/destroy.json"
          request.uri.rawQueryString === Some("screen_name=marcobonzanini")
        }
        .respondWith("/twitter/rest/friendships/unfollow.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/friendships/unfollow.json")
    }

    "unfollow a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: User = when(unfollowUserId(19018614L))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/destroy.json"
          request.uri.rawQueryString === Some("user_id=19018614")
        }
        .respondWith("/twitter/rest/friendships/unfollow.json")
        .await
      result === loadJsonAs[User]("/fixtures/rest/friendships/unfollow.json")
    }

    "enable retweets notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableRetweetsNotificationsForUser("marcobonzanini"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
          request.uri.rawQueryString === Some("retweets=true&screen_name=marcobonzanini")
        }
        .respondWith("/twitter/rest/friendships/update.json")
        .await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "enable retweets notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableRetweetsNotificationsForUserId(19018614L))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
          request.uri.rawQueryString === Some("retweets=true&user_id=19018614")
        }
        .respondWith("/twitter/rest/friendships/update.json")
        .await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "disable retweets notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableRetweetsNotificationsForUser("marcobonzanini"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
          request.uri.rawQueryString === Some("retweets=false&screen_name=marcobonzanini")
        }
        .respondWith("/twitter/rest/friendships/update.json")
        .await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "disable retweets notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableRetweetsNotificationsForUserId(19018614L))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
          request.uri.rawQueryString === Some("retweets=false&user_id=19018614")
        }
        .respondWith("/twitter/rest/friendships/update.json")
        .await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "enable device notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableDeviceNotificationsForUser("marcobonzanini"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
          request.uri.rawQueryString === Some("device=true&screen_name=marcobonzanini")
        }
        .respondWith("/twitter/rest/friendships/update.json")
        .await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "enable device notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableDeviceNotificationsForUserId(19018614L))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
          request.uri.rawQueryString === Some("device=true&user_id=19018614")
        }
        .respondWith("/twitter/rest/friendships/update.json")
        .await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "disable device notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableDeviceNotificationsForUser("marcobonzanini"))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
          request.uri.rawQueryString === Some("device=false&screen_name=marcobonzanini")
        }
        .respondWith("/twitter/rest/friendships/update.json")
        .await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "disable device notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableDeviceNotificationsForUserId(19018614L))
        .expectRequest { request =>
          request.method === HttpMethods.POST
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
          request.uri.rawQueryString === Some("device=false&user_id=19018614")
        }
        .respondWith("/twitter/rest/friendships/update.json")
        .await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "get a relationship by ids" in new TwitterFriendshipClientSpecContext {
      val result: RatedData[Relationship] = when(relationshipBetweenUserIds(2911461333L, 19018614L))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/show.json"
          request.uri.rawQueryString === Some("source_id=2911461333&target_id=19018614")
        }
        .respondWithRated("/twitter/rest/friendships/relationship.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Relationship]("/fixtures/rest/friendships/relationship.json")
    }

    "get a relationship by screen names" in new TwitterFriendshipClientSpecContext {
      val result: RatedData[Relationship] = when(relationshipBetweenUsers("DanielaSfregola", "marcobonzanini"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/show.json"
          request.uri.rawQueryString === Some("source_screen_name=DanielaSfregola&target_screen_name=marcobonzanini")
        }
        .respondWithRated("/twitter/rest/friendships/relationship.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Relationship]("/fixtures/rest/friendships/relationship.json")
    }

    "get relationships with a list of users" in new TwitterFriendshipClientSpecContext {
      val result: RatedData[Seq[LookupRelationship]] = when(relationshipsWithUsers("marcobonzanini", "odersky"))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/lookup.json"
          request.uri.rawQueryString === Some("screen_name=marcobonzanini%2Codersky")
        }
        .respondWithRated("/twitter/rest/friendships/relationships.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[LookupRelationship]]("/fixtures/rest/friendships/relationships.json")
    }

    "reject request if no ids have been provided for the lookup" in new TwitterFriendshipClientSpecContext {
      relationshipsWithUsers() must throwA[IllegalArgumentException](
        "requirement failed: please, provide at least one screen name")
    }

    "get relationships with a list of users by user id" in new TwitterFriendshipClientSpecContext {
      val result: RatedData[Seq[LookupRelationship]] = when(relationshipsWithUserIds(2911461333L, 2911461334L))
        .expectRequest { request =>
          request.method === HttpMethods.GET
          request.uri.endpoint === "https://api.twitter.com/1.1/friendships/lookup.json"
          request.uri.rawQueryString === Some("user_id=2911461333%2C2911461334")
        }
        .respondWithRated("/twitter/rest/friendships/relationships.json")
        .await
      result.rate_limit === rateLimit
      result.data === loadJsonAs[Seq[LookupRelationship]]("/fixtures/rest/friendships/relationships.json")
    }

    "reject request if no ids have been provided for the lookup" in new TwitterFriendshipClientSpecContext {
      relationshipsWithUserIds() must throwA[IllegalArgumentException](
        "requirement failed: please, provide at least one user id")
    }
  }
}

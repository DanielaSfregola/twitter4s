package twitter4s.http.clients.friendships

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities._
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterFriendshipClientSpec extends ClientSpec {

  trait TwitterFriendshipClientSpecContext extends ClientSpecContext with TwitterFriendshipClient

  "Twitter Friendship Client" should {

    "get all blocked users" in new TwitterFriendshipClientSpecContext {
      val result: Seq[Long] = when(blockedUsersIds).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/no_retweets/ids.json"
        request.uri.query === Query("stringify_ids=false")
      }.respondWith("/twitter/friendships/blocked_users.json").await
      result === loadJsonAs[Seq[Long]]("/fixtures/friendships/blocked_users.json")
    }

    "get all blocked users stringified" in new TwitterFriendshipClientSpecContext {
      val result: Seq[String] = when(blockedUsersIdsStringified).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/no_retweets/ids.json"
        request.uri.query === Query("stringify_ids=true")
      }.respondWith("/twitter/friendships/blocked_users_stringified.json").await
      result === loadJsonAs[Seq[String]]("/fixtures/friendships/blocked_users_stringified.json")
    }

    "get incoming friendships" in new TwitterFriendshipClientSpecContext {
      val result: UserIds = when(incomingFriendships()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/incoming.json"
        request.uri.query === Query("cursor=-1&stringify_ids=false")
      }.respondWith("/twitter/friendships/incoming_friendships_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/friendships/incoming_friendships_ids.json")
    }

    "get incoming friendships stringified" in new TwitterFriendshipClientSpecContext {
      val result: UserIdsStringified = when(incomingFriendshipsStringified()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/incoming.json"
        request.uri.query === Query("cursor=-1&stringify_ids=true")
      }.respondWith("/twitter/friendships/incoming_friendships_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/friendships/incoming_friendships_ids_stringified.json")
    }

    "get outgoing friendships" in new TwitterFriendshipClientSpecContext {
      val result: UserIds = when(outgoingFriendships()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/outgoing.json"
        request.uri.query === Query("cursor=-1&stringify_ids=false")
      }.respondWith("/twitter/friendships/outgoing_friendships_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/friendships/outgoing_friendships_ids.json")
    }

    "get outgoing friendships stringified" in new TwitterFriendshipClientSpecContext {
      val result: UserIdsStringified = when(outgoingFriendshipsStringified()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/outgoing.json"
        request.uri.query === Query("cursor=-1&stringify_ids=true")
      }.respondWith("/twitter/friendships/outgoing_friendships_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/friendships/outgoing_friendships_ids_stringified.json")
    }

    "follow a user" in new TwitterFriendshipClientSpecContext {
      val result: User = when(follow("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/create.json"
        request.uri.query === Query("follow=true&screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/follow.json").await
      result === loadJsonAs[User]("/fixtures/friendships/follow.json")
    }

    "follow a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: User = when(followUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/create.json"
        request.uri.query === Query("follow=true&user_id=19018614")
      }.respondWith("/twitter/friendships/follow.json").await
      result === loadJsonAs[User]("/fixtures/friendships/follow.json")
    }

    "unfollow a user" in new TwitterFriendshipClientSpecContext {
      val result: User = when(unfollow("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/destroy.json"
        request.uri.query === Query("screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/unfollow.json").await
      result === loadJsonAs[User]("/fixtures/friendships/unfollow.json")
    }

    "unfollow a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: User = when(unfollow(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/destroy.json"
        request.uri.query === Query("user_id=19018614")
      }.respondWith("/twitter/friendships/unfollow.json").await
      result === loadJsonAs[User]("/fixtures/friendships/unfollow.json")
    }
    
    "enable retweets notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableRetweetsNotifications("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("retweets=true&screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "enable retweets notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableRetweetsNotifications(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("retweets=true&user_id=19018614")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "disable retweets notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableRetweetsNotifications("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("retweets=false&screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "disable retweets notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableRetweetsNotifications(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("retweets=false&user_id=19018614")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "enable device notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableDeviceNotifications("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("device=true&screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "enable device notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableDeviceNotifications(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("device=true&user_id=19018614")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "disable device notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableDeviceNotifications("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("device=false&screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }

    "disable device notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableDeviceNotifications(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("device=false&user_id=19018614")
      }.respondWith("/twitter/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/update.json")
    }
    
    "get a relationship by ids" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(relationship(2911461333L, 19018614L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/show.json"
        request.uri.query === Query("source_id=2911461333&target_id=19018614")
      }.respondWith("/twitter/friendships/relationship.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/relationship.json")
    }
    
    "get a relationship by screen names" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(relationship("DanielaSfregola", "marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/show.json"
        request.uri.query === Query("source_screen_name=DanielaSfregola&target_screen_name=marcobonzanini")
      }.respondWith("/twitter/friendships/relationship.json").await
      result === loadJsonAs[Relationship]("/fixtures/friendships/relationship.json")
    }

    "get relationships with a list of users" in new TwitterFriendshipClientSpecContext {
      val result: Seq[LookupRelationship] = when(relationships("marcobonzanini", "odersky")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/lookup.json"
        request.uri.query === Query("screen_name=marcobonzanini,odersky")
      }.respondWith("/twitter/friendships/relationships.json").await
      result === loadJsonAs[Seq[LookupRelationship]]("/fixtures/friendships/relationships.json")
    }

    "reject request if no ids have been provided for the lookup" in new TwitterFriendshipClientSpecContext {
      relationships() must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }

    "get relationships with a list of users by user id" in new TwitterFriendshipClientSpecContext {
      val result: Seq[LookupRelationship] = when(relationshipsByUserIds(2911461333L, 2911461334L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/lookup.json"
        request.uri.query === Query("user_id=2911461333,2911461334")
      }.respondWith("/twitter/friendships/relationships.json").await
      result === loadJsonAs[Seq[LookupRelationship]]("/fixtures/friendships/relationships.json")
    }

    "reject request if no ids have been provided for the lookup" in new TwitterFriendshipClientSpecContext {
      relationshipsByUserIds() must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }
  }
}

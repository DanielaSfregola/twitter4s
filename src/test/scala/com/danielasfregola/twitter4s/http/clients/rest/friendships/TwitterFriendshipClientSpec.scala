package com.danielasfregola.twitter4s.http.clients.rest.friendships


import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities._

class TwitterFriendshipClientSpec extends ClientSpec {

  class TwitterFriendshipClientSpecContext extends ClientSpecContext with TwitterFriendshipClient

  "Twitter Friendship Client" should {

    "get all blocked users" in new TwitterFriendshipClientSpecContext {
      val result: Seq[Long] = when(getNoRetweetsUserIds).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/no_retweets/ids.json"
        request.uri.query === Query("stringify_ids=false")
      }.respondWith("/twitter/rest/friendships/blocked_users.json").await
      result === loadJsonAs[Seq[Long]]("/fixtures/rest/friendships/blocked_users.json")
    }

    "get all blocked users stringified" in new TwitterFriendshipClientSpecContext {
      val result: Seq[String] = when(getNoRetweetsUserStringifiedIds).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/no_retweets/ids.json"
        request.uri.query === Query("stringify_ids=true")
      }.respondWith("/twitter/rest/friendships/blocked_users_stringified.json").await
      result === loadJsonAs[Seq[String]]("/fixtures/rest/friendships/blocked_users_stringified.json")
    }

    "get incoming friendships" in new TwitterFriendshipClientSpecContext {
      val result: UserIds = when(getIncomingFriendshipIds()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/incoming.json"
        request.uri.query === Query("cursor=-1&stringify_ids=false")
      }.respondWith("/twitter/rest/friendships/incoming_friendships_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/rest/friendships/incoming_friendships_ids.json")
    }

    "get incoming friendships stringified" in new TwitterFriendshipClientSpecContext {
      val result: UserStringifiedIds = when(getIncomingFriendshipStringifiedIds()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/incoming.json"
        request.uri.query === Query("cursor=-1&stringify_ids=true")
      }.respondWith("/twitter/rest/friendships/incoming_friendships_ids_stringified.json").await
      result === loadJsonAs[UserStringifiedIds]("/fixtures/rest/friendships/incoming_friendships_ids_stringified.json")
    }

    "get outgoing friendships" in new TwitterFriendshipClientSpecContext {
      val result: UserIds = when(getOutgoingFriendshipIds()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/outgoing.json"
        request.uri.query === Query("cursor=-1&stringify_ids=false")
      }.respondWith("/twitter/rest/friendships/outgoing_friendships_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/rest/friendships/outgoing_friendships_ids.json")
    }

    "get outgoing friendships stringified" in new TwitterFriendshipClientSpecContext {
      val result: UserStringifiedIds = when(getOutgoingFriendshipStringifiedIds()).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/outgoing.json"
        request.uri.query === Query("cursor=-1&stringify_ids=true")
      }.respondWith("/twitter/rest/friendships/outgoing_friendships_ids_stringified.json").await
      result === loadJsonAs[UserStringifiedIds]("/fixtures/rest/friendships/outgoing_friendships_ids_stringified.json")
    }

    "follow a user" in new TwitterFriendshipClientSpecContext {
      val result: User = when(followUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/create.json"
        request.uri.query === Query("follow=true&screen_name=marcobonzanini")
      }.respondWith("/twitter/rest/friendships/follow.json").await
      result === loadJsonAs[User]("/fixtures/rest/friendships/follow.json")
    }

    "follow a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: User = when(followUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/create.json"
        request.uri.query === Query("follow=true&user_id=19018614")
      }.respondWith("/twitter/rest/friendships/follow.json").await
      result === loadJsonAs[User]("/fixtures/rest/friendships/follow.json")
    }

    "unfollow a user" in new TwitterFriendshipClientSpecContext {
      val result: User = when(unfollowUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/destroy.json"
        request.uri.query === Query("screen_name=marcobonzanini")
      }.respondWith("/twitter/rest/friendships/unfollow.json").await
      result === loadJsonAs[User]("/fixtures/rest/friendships/unfollow.json")
    }

    "unfollow a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: User = when(unfollowUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/destroy.json"
        request.uri.query === Query("user_id=19018614")
      }.respondWith("/twitter/rest/friendships/unfollow.json").await
      result === loadJsonAs[User]("/fixtures/rest/friendships/unfollow.json")
    }
    
    "enable retweets notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableRetweetsNotificationsForUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("retweets=true&screen_name=marcobonzanini")
      }.respondWith("/twitter/rest/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "enable retweets notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableRetweetsNotificationsForUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("retweets=true&user_id=19018614")
      }.respondWith("/twitter/rest/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "disable retweets notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableRetweetsNotificationsForUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("retweets=false&screen_name=marcobonzanini")
      }.respondWith("/twitter/rest/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "disable retweets notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableRetweetsNotificationsForUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("retweets=false&user_id=19018614")
      }.respondWith("/twitter/rest/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "enable device notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableDeviceNotificationsForUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("device=true&screen_name=marcobonzanini")
      }.respondWith("/twitter/rest/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "enable device notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(enableDeviceNotificationsForUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("device=true&user_id=19018614")
      }.respondWith("/twitter/rest/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "disable device notifications for a user" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableDeviceNotificationsForUser("marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("device=false&screen_name=marcobonzanini")
      }.respondWith("/twitter/rest/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }

    "disable device notifications for a user by user id" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(disableDeviceNotificationsForUserId(19018614L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/update.json"
        request.uri.query === Query("device=false&user_id=19018614")
      }.respondWith("/twitter/rest/friendships/update.json").await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/update.json")
    }
    
    "get a relationship by ids" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(getRelationshipBetweenUserIds(2911461333L, 19018614L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/show.json"
        request.uri.query === Query("source_id=2911461333&target_id=19018614")
      }.respondWith("/twitter/rest/friendships/relationship.json").await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/relationship.json")
    }
    
    "get a relationship by screen names" in new TwitterFriendshipClientSpecContext {
      val result: Relationship = when(getRelationshipBetweenUsers("DanielaSfregola", "marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/show.json"
        request.uri.query === Query("source_screen_name=DanielaSfregola&target_screen_name=marcobonzanini")
      }.respondWith("/twitter/rest/friendships/relationship.json").await
      result === loadJsonAs[Relationship]("/fixtures/rest/friendships/relationship.json")
    }

    "get relationships with a list of users" in new TwitterFriendshipClientSpecContext {
      val result: Seq[LookupRelationship] = when(getRelationshipsWithUsers("marcobonzanini", "odersky")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/lookup.json"
        request.uri.query === Query("screen_name=marcobonzanini,odersky")
      }.respondWith("/twitter/rest/friendships/relationships.json").await
      result === loadJsonAs[Seq[LookupRelationship]]("/fixtures/rest/friendships/relationships.json")
    }

    "reject request if no ids have been provided for the lookup" in new TwitterFriendshipClientSpecContext {
      getRelationshipsWithUsers() must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }

    "get relationships with a list of users by user id" in new TwitterFriendshipClientSpecContext {
      val result: Seq[LookupRelationship] = when(getRelationshipsWithUserIds(2911461333L, 2911461334L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/friendships/lookup.json"
        request.uri.query === Query("user_id=2911461333,2911461334")
      }.respondWith("/twitter/rest/friendships/relationships.json").await
      result === loadJsonAs[Seq[LookupRelationship]]("/fixtures/rest/friendships/relationships.json")
    }

    "reject request if no ids have been provided for the lookup" in new TwitterFriendshipClientSpecContext {
      getRelationshipsWithUserIds() must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }
  }
}

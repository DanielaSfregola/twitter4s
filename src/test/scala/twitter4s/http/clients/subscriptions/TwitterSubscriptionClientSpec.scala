package twitter4s.http.clients.subscriptions

import spray.http.HttpMethods
import spray.http.Uri.Query
import twitter4s.entities.{Tweet, Subscription}
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterSubscriptionClientSpec  extends ClientSpec {

  trait TwitterSubscriptionClientSpecContext extends ClientSpecContext with TwitterSubscriptionClient

  "Twitter Subscription Client" should {

    "get subscriptions for a user" in new TwitterSubscriptionClientSpecContext {
      val result: Seq[Subscription] = when(subscriptions("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/list.json"
        request.uri.query === Query("reverse=false&screen_name=DanielaSfregola")
      }.respondWith("/twitter/subscriptions/subscriptions.json").await
      result === loadJsonAs[Seq[Subscription]]("/fixtures/subscriptions/subscriptions.json")
    }

    "get subscriptions for a user by id" in new TwitterSubscriptionClientSpecContext {
      val result: Seq[Subscription] = when(subscriptionsPerUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/list.json"
        request.uri.query === Query("reverse=false&user_id=2911461333")
      }.respondWith("/twitter/subscriptions/subscriptions.json").await
      result === loadJsonAs[Seq[Subscription]]("/fixtures/subscriptions/subscriptions.json")
    }

    "get a subscription timeline by id" in new TwitterSubscriptionClientSpecContext {
      val result: Seq[Tweet] = when(subscriptionTimelinePerId(8044403)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/statuses.json"
        request.uri.query === Query("count=20&include_entities=true&include_rts=false&list_id=8044403")
      }.respondWith("/twitter/subscriptions/subscription_timeline.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/subscriptions/subscription_timeline.json")
    }

    "get a subscription timeline by slug and owner" in new TwitterSubscriptionClientSpecContext {
      val result: Seq[Tweet] = when(subscriptionTimelinePerSlugAndOwner("meetup-20100301", "twitterapi")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/statuses.json"
        request.uri.query === Query("count=20&include_entities=true&include_rts=false&owner_screen_name=twitterapi&slug=meetup-20100301")
      }.respondWith("/twitter/subscriptions/subscription_timeline.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/subscriptions/subscription_timeline.json")
    }

    "get a subscription timeline by slug and owner id" in new TwitterSubscriptionClientSpecContext {
      val result: Seq[Tweet] = when(subscriptionTimelinePerSlugAndOwnerId("meetup-20100301", 6253282)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/statuses.json"
        request.uri.query === Query("count=20&include_entities=true&include_rts=false&owner_id=6253282&slug=meetup-20100301")
      }.respondWith("/twitter/subscriptions/subscription_timeline.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/subscriptions/subscription_timeline.json")
    }

    "remove member by subscription id and user" in new TwitterSubscriptionClientSpecContext {
      val result: Unit = when(removeMemberPerId(8044403, "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("list_id=8044403&screen_name=DanielaSfregola")
      }.respondWithOk.await
      result === ()
    }

    "remove member by slug and owner and user" in new TwitterSubscriptionClientSpecContext {
      val result: Unit = when(removeMemberPerSlugAndOwner("meetup-20100301", "twitterapi", "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("owner_screen_name=twitterapi&screen_name=DanielaSfregola&slug=meetup-20100301")
      }.respondWithOk.await
      result === ()
    }

    "remove member by slug and owner id and user" in new TwitterSubscriptionClientSpecContext {
      val result: Unit = when(removeMemberPerSlugAndOwnerId("meetup-20100301", 6253282, "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("owner_id=6253282&screen_name=DanielaSfregola&slug=meetup-20100301")
      }.respondWithOk.await
      result === ()
    }

    "remove member by subscription id and user id" in new TwitterSubscriptionClientSpecContext {
      val result: Unit = when(removeMemberIdPerId(8044403, 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("list_id=8044403&user_id=2911461333")
      }.respondWithOk.await
      result === ()
    }

    "remove member by slug and owner and user id" in new TwitterSubscriptionClientSpecContext {
      val result: Unit = when(removeMemberIdPerSlugAndOwner("meetup-20100301", "twitterapi", 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("owner_screen_name=twitterapi&slug=meetup-20100301&user_id=2911461333")
      }.respondWithOk.await
      result === ()
    }

    "remove member by slug and owner id and user id" in new TwitterSubscriptionClientSpecContext {
      val result: Unit = when(removeMemberIdPerSlugAndOwnerId("meetup-20100301", 6253282, 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("owner_id=6253282&slug=meetup-20100301&user_id=2911461333")
      }.respondWithOk.await
      result === ()
    }
  }

}

package twitter4s.http.clients.statuses

import spray.http.{ContentType, MediaTypes, HttpEntity, HttpMethods}
import spray.http.Uri.Query
import twitter4s.entities._
import twitter4s.util.{ClientSpec, ClientSpecContext}

class TwitterStatusClientSpec extends ClientSpec {

  trait TwitterStatusClientSpecContext extends ClientSpecContext with TwitterStatusClient

  "Twitter Status Client" should {

    "perform a mentions timeline request" in new TwitterStatusClientSpecContext {
      val result: Seq[Tweet] = when(mentionsTimeline(count = 10)).expectRequest { request =>
                    request.method === HttpMethods.GET
                    request.uri.endpoint === "https://api.twitter.com/1.1/statuses/mentions_timeline.json"
                    request.uri.query === Query("contributor_details=false&count=10&include_entities=true&trim_user=false")
                  }.respondWith("/twitter/statuses/mentions_timeline.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/statuses/mentions_timeline.json")
    }

    "perform a user timeline request" in new TwitterStatusClientSpecContext {
      val result: Seq[Tweet] = when(userTimeline(count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/user_timeline.json"
        request.uri.query === Query("contributor_details=false&count=10&exclude_replies=false&include_rts=true&trim_user=false")
      }.respondWith("/twitter/statuses/user_timeline.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/statuses/user_timeline.json")
    }

    "perform a home timeline request" in new TwitterStatusClientSpecContext {
      val result: Seq[Tweet] = when(homeTimeline(count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/home_timeline.json"
        request.uri.query === Query("contributor_details=false&count=10&exclude_replies=false&include_entities=true&trim_user=false")
      }.respondWith("/twitter/statuses/home_timeline.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/statuses/home_timeline.json")
    }

    "perform a retweets of me request" in new TwitterStatusClientSpecContext {
      val result: Seq[Tweet] = when(retweetsOfMe(count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/retweets_of_me.json"
        request.uri.query === Query("contributor_details=false&count=10&exclude_replies=false&include_entities=true&trim_user=false")
      }.respondWith("/twitter/statuses/retweets_of_me.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/statuses/retweets_of_me.json")
    }

    "perform a retweets request" in new TwitterStatusClientSpecContext {
      val id = 648866645855879168L
      val result: Seq[Tweet] = when(retweets(id, count = 10)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === s"https://api.twitter.com/1.1/statuses/retweets/$id.json"
        request.uri.query === Query("count=10&trim_user=false")
      }.respondWith("/twitter/statuses/retweets.json").await
      result === loadJsonAs[Seq[Tweet]]("/fixtures/statuses/retweets.json")
    }

    "perform a show tweet request" in new TwitterStatusClientSpecContext {
      val id = 648866645855879168L
      val result: Tweet = when(show(id)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/show.json"
        request.uri.query === Query("id=648866645855879168&include_entities=true&include_my_retweet=false&trim_user=false")
      }.respondWith("/twitter/statuses/show.json").await
      result === loadJsonAs[Tweet]("/fixtures/statuses/show.json")
    }

    "send a status update" in new TwitterStatusClientSpecContext {
      val result: Tweet = when(updateStatus("This is a test")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/update.json"
        request.entity === HttpEntity(
          ContentType(MediaTypes.`application/x-www-form-urlencoded`),
          "display_coordinates=false&possibly_sensitive=false&status=This+is+a+test&trim_user=false")
      }.respondWith("/twitter/statuses/update.json").await
      result === loadJsonAs[Tweet]("/fixtures/statuses/update.json")
    }

    "send direct message as tweet" in new TwitterStatusClientSpecContext {
      val result: Tweet = when(createDirectMessageAsTweet("This is a test for a direct message", "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/update.json"
        request.entity === HttpEntity(
          ContentType(MediaTypes.`application/x-www-form-urlencoded`),
          "display_coordinates=false&possibly_sensitive=false&status=D+DanielaSfregola+This+is+a+test+for+a+direct+message&trim_user=false")
      }.respondWith("/twitter/statuses/direct_message.json").await
      result === loadJsonAs[Tweet]("/fixtures/statuses/direct_message.json")
    }

    "delete an existing tweet" in new TwitterStatusClientSpecContext {
      val id = 648866645855879168L
      val result: Tweet = when(destroyStatus(id)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === s"https://api.twitter.com/1.1/statuses/destroy/$id.json"
        request.uri.query === Query("trim_user=false")
      }.respondWith("/twitter/statuses/destroy.json").await
      result === loadJsonAs[Tweet]("/fixtures/statuses/destroy.json")
    }

    "retweet a tweet" in new TwitterStatusClientSpecContext {
      val id = 648866645855879168L
      val result: Tweet = when(retweet(id)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === s"https://api.twitter.com/1.1/statuses/retweet/$id.json"
        request.uri.query === Query("trim_user=false")
      }.respondWith("/twitter/statuses/retweet.json").await
      result === loadJsonAs[Tweet]("/fixtures/statuses/retweet.json")
    }

    "get a tweet by id in oembed format " in new TwitterStatusClientSpecContext {
      val id = 648866645855879168L
      val result: OEmbedTweet = when(oembedById(id)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/oembed.json"
        request.uri.query === Query("align=none&hide_media=false&hide_thread=false&hide_tweet=false&id=648866645855879168&lang=en&omit_script=false")
      }.respondWith("/twitter/statuses/oembed.json").await
      result === loadJsonAs[OEmbedTweet]("/fixtures/statuses/oembed.json")
    }

    "get a tweet by url in oembed format " in new TwitterStatusClientSpecContext {
      val url = s"https://twitter.com/Interior/status/648866645855879168"
      val result: OEmbedTweet = when(oembedByUrl(url)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/oembed.json"
        request.uri.query === Query("align=none&hide_media=false&hide_thread=false&hide_tweet=false&lang=en&omit_script=false&url=https%253A%252F%252Ftwitter.com%252FInterior%252Fstatus%252F648866645855879168")
      }.respondWith("/twitter/statuses/oembed.json").await
      result === loadJsonAs[OEmbedTweet]("/fixtures/statuses/oembed.json")
    }

    "get retweeters ids" in new TwitterStatusClientSpecContext {
      val result: UserIds = when(retweetersIds(327473909412814850L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/retweeters/ids.json"
        request.uri.query === Query("count=100&cursor=-1&id=327473909412814850&stringify_ids=false")
      }.respondWith("/twitter/statuses/retweeters_ids.json").await
      result === loadJsonAs[UserIds]("/fixtures/statuses/retweeters_ids.json")
    }

    "get retweeters ids stringified" in new TwitterStatusClientSpecContext {
      val result: UserIdsStringified = when(retweetersIdsStringified(327473909412814850L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/retweeters/ids.json"
        request.uri.query === Query("count=100&cursor=-1&id=327473909412814850&stringify_ids=true")
      }.respondWith("/twitter/statuses/retweeters_ids_stringified.json").await
      result === loadJsonAs[UserIdsStringified]("/fixtures/statuses/retweeters_ids_stringified.json")
    }

    "perform a lookup" in new TwitterStatusClientSpecContext {
      val result: Seq[LookupTweet] = when(lookup(327473909412814850L, 327473909412814851L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/lookup.json"
        request.uri.query === Query("id=327473909412814850,327473909412814851&include_entities=true&map=false&trim_user=false")
      }.respondWith("/twitter/statuses/lookup.json").await
      result === loadJsonAs[Seq[LookupTweet]]("/fixtures/statuses/lookup.json")
    }

    "perform a mapped lookup" in new TwitterStatusClientSpecContext {
      val result: LookupMapped = when(lookupMapped(327473909412814850L, 327473909412814851L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/statuses/lookup.json"
        request.uri.query === Query("id=327473909412814850,327473909412814851&include_entities=true&map=true&trim_user=false")
      }.respondWith("/twitter/statuses/lookup_mapped.json").await
      result === loadJsonAs[LookupMapped]("/fixtures/statuses/lookup_mapped.json")
    }
  }
}


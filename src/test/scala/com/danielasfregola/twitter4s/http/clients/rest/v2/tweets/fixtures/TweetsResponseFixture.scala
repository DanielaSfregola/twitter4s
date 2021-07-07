package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.fixtures

import com.danielasfregola.twitter4s.entities.v2._
import com.danielasfregola.twitter4s.entities.v2.enums.{CoordinatesType, ReferencedTweetType, TweetReplySetting}
import com.danielasfregola.twitter4s.entities.v2.responses.TweetsResponse
import java.time.Instant

object TweetsResponseFixture {
  val fixture: TweetsResponse = TweetsResponse(
    data = Seq(Tweet(
      id = "2310484964373377688",
      text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
      attachments = Some(TweetAttachments(
        media_keys = Seq(
          "13_9995568729608410852",
          "14_0381608460867506993"
        ),
        poll_ids = Seq(
          "0419198663175162881"
        )
      )),
      author_id = Some("6338724728067829004"),
      context_annotations = Some(Seq(
        TweetContextAnnotation(
          domain = TweetDomain(
            id = "10",
            name = "Person",
            description = Some("Vestibulum pellentesque sed justo ac lacinia")
          ),
          entity = TweetEntity(
            id = "5233852574321016658",
            name = "Phasellus Eu",
            description = Some("Phasellus Eu")
          )
        ),
        TweetContextAnnotation(
          domain = TweetDomain(
            id = "54",
            name = "Musician",
            description = Some("Nullam imperdiet lectus metus")
          ),
          entity = TweetEntity(
            id = "6836362243680041612",
            name = "Phasellus Eu",
            description = Some("Phasellus Eu")
          )
        )
      )),
      conversation_id = Some("0471114572504780656"),
      created_at = Some(Instant.parse("2020-05-15T16:03:42.000Z")),
      entities = Some(TweetEntities(
        annotations = Seq(
          TweetEntitiesAnnotation(
            start = 144,
            end = 150,
            probability = 0.626f,
            `type` = "Product",
            normalized_text = "Twitter"
          )
        ),
        cashtags = Seq(
          TweetEntitiesCashtag(
            start = 41,
            end = 44,
            tag = "GE"
          )
        ),
        urls = Seq(
          TweetEntitiesURL(
            start = 257,
            end = 280,
            url = "https://t.co/sodales",
            expanded_url = "https://www.google.com/sodales",
            display_url = "example.google.com/sodales",
            unwound_url = Some("https://www.google.com/sodales")
          ),
        ),
        mentions = Seq(
          TweetEntitiesMention(
            start = 105,
            end = 121,
            username = Some("SuspendisseAtNunc"),
            id = Some("2894469526322928935")
          ),
          TweetEntitiesMention(
            start = 125,
            end = 138,
            username = Some("SuspendisseAtNuncPosuere"),
            id = Some("6279687081065223918")
          )
        ),
        hashtags = Seq(
          TweetEntitiesHashtag(
            start = 47,
            end = 60,
            tag = "SuspendisseAtNunc"
          ),
          TweetEntitiesHashtag(
            start = 171,
            end = 194,
            tag = "SuspendisseNunc"
          )
        ),
      )),
      geo = Some(TweetGeo(
        coordinates = Some(TweetCoordinates(
          `type` = CoordinatesType.Point,
          coordinates = (40.74118764, -73.9998279)
        )),
        place_id = Some("0fc2bbe1f995b733")
      )),
      in_reply_to_user_id = Some("1600750904601052113"),
      lang = Some("en"),
      non_public_metrics = Some(TweetNonPublicMetrics(
        user_profile_clicks = 0,
        impression_count = 29,
        url_link_clicks = Some(12)
      )),
      organic_metrics = Some(TweetOrganicMetrics(
        retweet_count = 0,
        url_link_clicks = Some(12),
        reply_count = 0,
        like_count = 1,
        user_profile_clicks = 0,
        impression_count = 29
      )),
      possibly_sensitive = Some(true),
      promoted_metrics = Some(TweetPromotedMetrics(
        impression_count = 29,
        url_link_clicks = Some(12),
        user_profile_clicks = 0,
        retweet_count = 0,
        reply_count = 0,
        like_count = 1
      )),
      public_metrics = Some(TweetPublicMetrics(
        retweet_count = 0,
        reply_count = 0,
        like_count = 1,
        quote_count = 0
      )),
      referenced_tweets = Some(Seq(
        TweetReferencedTweet(
          `type` = ReferencedTweetType.Retweeted,
          id = "4653693971459419590"
        )
      )),
      reply_settings = Some(TweetReplySetting.Everyone),
      source = Some("Twitter for iPhone"),
      withheld = None
    )),
    includes = Some(Includes(
      tweets = Seq(Tweet(
        id = "6304480225832455363",
        text = "Donec feugiat elit tellus, a ultrices elit sodales facilisis.",
        attachments = None,
        author_id = None,
        context_annotations = None,
        conversation_id = None,
        created_at = None,
        entities = None,
        geo = None,
        in_reply_to_user_id = None,
        lang = None,
        non_public_metrics = None,
        organic_metrics = None,
        possibly_sensitive = None,
        promoted_metrics = None,
        public_metrics = None,
        referenced_tweets = None,
        reply_settings = None,
        source = None,
        withheld = None
      ))
    )),
    errors = None
  )
}

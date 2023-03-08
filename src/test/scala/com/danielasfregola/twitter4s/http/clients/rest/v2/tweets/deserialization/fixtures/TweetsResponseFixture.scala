package com.danielasfregola.twitter4s.http.clients.rest.v2.tweets.deserialization.fixtures

import com.danielasfregola.twitter4s.entities.v2.enums._
import com.danielasfregola.twitter4s.entities.v2.responses.TweetsResponse
import com.danielasfregola.twitter4s.entities.v2._
import java.time.Instant

object TweetsResponseFixture {
  val fixture: TweetsResponse = TweetsResponse(
    data = Seq(
      Tweet(
        id = "2310484964373377688",
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        attachments = Some(
          TweetAttachments(
            media_keys = Seq(
              "13_9995568729608410852",
              "14_0381608460867506993",
              "7_1422932273641500672",
              "3_1422208929984139264"
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
        geo = Some(
          TweetGeo(
            coordinates = Some(
              TweetCoordinates(
                `type` = CoordinatesType.Point,
                coordinates = (40.74118764, -73.9998279)
              )),
            place_id = Some("0fc2bbe1f995b733")
          )),
        in_reply_to_user_id = Some("1600750904601052113"),
        lang = Some("en"),
        non_public_metrics = Some(
          TweetNonPublicMetrics(
            user_profile_clicks = 0,
            impression_count = 29,
            url_link_clicks = Some(12)
          )),
        organic_metrics = Some(
          TweetOrganicMetrics(
            retweet_count = 0,
            url_link_clicks = Some(12),
            reply_count = 0,
            like_count = 1,
            user_profile_clicks = 0,
            impression_count = 29
          )),
        possibly_sensitive = Some(true),
        promoted_metrics = Some(
          TweetPromotedMetrics(
            impression_count = 29,
            url_link_clicks = Some(12),
            user_profile_clicks = 0,
            retweet_count = 0,
            reply_count = 0,
            like_count = 1
          )),
        public_metrics = Some(
          TweetPublicMetrics(
            retweet_count = 0,
            reply_count = 0,
            like_count = 1,
            quote_count = 0,
            impression_count = 0
          )),
        referenced_tweets = Some(
          Seq(
            TweetReferencedTweet(
              `type` = ReferencedTweetType.Retweeted,
              id = "4653693971459419590"
            )
          )),
        reply_settings = Some(TweetReplySetting.Everyone),
        source = Some("Twitter for iPhone"),
        withheld = None
      )),
    includes = Some(
      TweetIncludes(
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
        )),
        users = Seq(
          User(
            id = "3955854555026519618",
            name = "AliquamOrciEros",
            username = "aliquamorcieros",
            created_at = None,
            `protected` = None,
            withheld = None,
            location = None,
            url = None,
            description = None,
            verified = None,
            entities = None,
            profile_image_url = None,
            public_metrics = None,
            pinned_tweet_id = None
          ),
          User(
            id = "6747736441958634428",
            name = "Suspendisse At Nunc",
            username = "suspendisseatnunc",
            created_at = None,
            `protected` = None,
            withheld = None,
            location = None,
            url = None,
            description = None,
            verified = None,
            entities = None,
            profile_image_url = None,
            public_metrics = None,
            pinned_tweet_id = None
          )
        ),
        media = Seq(
          Media(
            media_key = "7_8340593532515834174",
            `type` = MediaType.Photo,
            duration_ms = None,
            height = Some(1280),
            non_public_metrics = None,
            organic_metrics = None,
            preview_image_url = None,
            promoted_metrics = None,
            public_metrics = None,
            url = None,
            width = None
          ),
          Media(
            media_key = "7_1422932273641500672",
            `type` = MediaType.Video,
            duration_ms = Some(70500),
            height = Some(450),
            non_public_metrics = Some(
              MediaNonPublicMetrics(
                playback_0_count = 1561,
                playback_100_count = 116,
                playback_25_count = 559,
                playback_50_count = 305,
                playback_75_count = 183
              )),
            organic_metrics = Some(
              MediaOrganicMetrics(
                playback_0_count = 1561,
                playback_100_count = 116,
                playback_25_count = 559,
                playback_50_count = 305,
                playback_75_count = 183,
                view_count = 629
              )),
            preview_image_url =
              Some("https://pbs.twimg.com/ext_tw_video_thumb/1422932273641500672/pu/img/kjOa5XKoU_UViWar.jpg"),
            promoted_metrics = Some(
              MediaPromotedMetrics(
                playback_0_count = 259,
                playback_100_count = 15,
                playback_25_count = 113,
                playback_50_count = 57,
                playback_75_count = 25,
                view_count = 124
              )),
            public_metrics = Some(
              MediaPublicMetrics(
                view_count = 1162
              )),
            url = None,
            width = Some(720)
          ),
          Media(
            media_key = "3_1422208929984139264",
            `type` = MediaType.AnimatedGIF,
            duration_ms = None,
            height = Some(720),
            non_public_metrics = None,
            organic_metrics = None,
            preview_image_url = None,
            promoted_metrics = None,
            public_metrics = None,
            url = Some("https://pbs.twimg.com/media/E7yzfJQX0AAw-u8.jpg"),
            width = Some(1280)
          )
        ),
        places = Seq(
          Place(
            full_name = "Seattle",
            id = "123",
            contained_within = Seq("987"),
            country = None,
            country_code = None,
            geo = Some(
              GeoJSON(
                """{"type":"Feature","geometry":{"type":"Point","coordinates":[47.6062,-122.3321]},"properties":{"name":"Seattle"}}"""
              )),
            name = None,
            place_type = None
          ),
          Place(
            full_name = "London, the capital and largest city of England and the United Kingdom",
            id = "456",
            contained_within = Seq.empty[String],
            country = Some("England"),
            country_code = Some("44"),
            geo = None,
            name = Some("London"),
            place_type = Some("city")
          )
        ),
        polls = Seq(
          Poll(
            id = "123",
            options = Seq(
              PollOption(
                position = 1,
                label = "Option A",
                votes = 795
              ),
              PollOption(
                position = 2,
                label = "Option B",
                votes = 800
              ),
            ),
            duration_minutes = Some(1440),
            end_datetime = None,
            voting_status = Some("closed")
          ),
          Poll(
            id = "123",
            options = Seq.empty[PollOption],
            duration_minutes = None,
            end_datetime = Some(Instant.parse("2019-11-28T20:26:41.000Z")),
            voting_status = None
          )
        )
      )),
    meta = Some(
      Meta(
        oldest_id = Some("1356759580211109999"),
        newest_id = Some("1410697282811569999"),
        result_count = 7,
        next_token = Some("123"),
        previous_token = None
      )),
    errors = Seq(
      Error(
        detail = "Some generic error",
        field = Some("archaeology"),
        parameter = Some("bones"),
        resource_id = Some("123"),
        resource_type = Some("tibula"),
        section = None,
        title = "One strange error",
        `type` = None,
        value = Some("123")
      ),
      Error(
        detail = "Some other generic error",
        field = None,
        parameter = Some("login"),
        resource_id = Some("123"),
        resource_type = Some("unknown"),
        section = Some("zero"),
        title = "Another strange error",
        `type` = Some("hidden"),
        value = None
      )
    )
  )
}

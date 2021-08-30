package com.danielasfregola.twitter4s.http.clients.rest.v2.users.deserialization.fixtures

import com.danielasfregola.twitter4s.entities.v2.responses.UserResponse
import com.danielasfregola.twitter4s.entities.v2._
import java.time.Instant

object UserResponseFixture {
  val fixture: UserResponse = UserResponse(
    data = Some(
      User(
        id = "6338724728067829004",
        name = "Shippy the Squirrel",
        username = "shippy",
        created_at = Some(Instant.parse("2020-05-15T16:03:42.000Z")),
        `protected` = Some(false),
        location = Some("Seattle"),
        url = Some("https://www.google.com/sodales"),
        description = Some("Sed efficitur ultrices elit sed volutpat."),
        verified = Some(true),
        entities = Some(UserEntities(
          url = Some(
            UserURLContainer(
              urls = Seq(
                UserEntitiesURL(
                  start = 257,
                  end = 280,
                  url = "https://t.co/sodales",
                  expanded_url = "https://www.google.com/sodales",
                  display_url = "example.google.com/sodales",
                )
              )
            )),
          description = Some(UserEntitiesDescription(
            urls = Seq(
              UserEntitiesURL(
                start = 257,
                end = 280,
                url = "https://t.co/sodales",
                expanded_url = "https://www.google.com/sodales",
                display_url = "example.google.com/sodales",
              )
            ),
            mentions = Seq(
              UserEntitiesMention(
                start = 105,
                end = 121,
                username = "SuspendisseAtNunc"
              ),
              UserEntitiesMention(
                start = 125,
                end = 138,
                username = "SuspendisseAtNuncPosuere"
              )
            ),
            hashtags = Seq(
              UserEntitiesHashtag(
                start = 47,
                end = 60,
                tag = "SuspendisseAtNunc"
              ),
              UserEntitiesHashtag(
                start = 171,
                end = 194,
                tag = "SuspendisseNunc"
              )
            ),
            cashtags = Seq(UserEntitiesCashtag(
              start = 41,
              end = 44,
              tag = "GE"
            ))
          ))
        )),
        profile_image_url = Some("https://www.google.com/sodales.adri"),
        public_metrics = Some(
          UserPublicMetrics(
            followers_count = 501796,
            following_count = 306,
            tweet_count = 6655,
            listed_count = 1433
          )),
        pinned_tweet_id = Some("2894469526322928935"),
        withheld = None
      )),
    includes = Some(
      UserIncludes(
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
        )
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

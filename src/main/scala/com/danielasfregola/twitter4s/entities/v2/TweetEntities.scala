package com.danielasfregola.twitter4s.entities.v2

final case class TweetEntities(annotations: Seq[TweetEntitiesAnnotation],
                               urls: Seq[TweetEntitiesURL],
                               hashtags: Seq[TweetEntitiesHashtag],
                               mentions: Seq[TweetEntitiesMention],
                               cashtags: Seq[TweetEntitiesCashtag])

final case class TweetEntitiesAnnotation(start: Int,
                                         end: Int,
                                         probability: Float,
                                         `type`: String,
                                         normalized_text: String)

final case class TweetEntitiesURL(start: Int,
                                  end: Int,
                                  url: String,
                                  expanded_url: String,
                                  display_url: String,
                                  unwound_url: Option[String])

final case class TweetEntitiesHashtag(start: Int, end: Int, tag: String)

final case class TweetEntitiesMention(start: Int, end: Int, username: Option[String], id: Option[String])

final case class TweetEntitiesCashtag(start: Int, end: Int, tag: String)

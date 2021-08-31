package com.danielasfregola.twitter4s.entities.v2

import com.danielasfregola.twitter4s.entities.v2.enums.CoordinatesType.CoordinatesType
import com.danielasfregola.twitter4s.entities.v2.enums.ReferencedTweetType.ReferencedTweetType
import com.danielasfregola.twitter4s.entities.v2.enums.TweetReplySetting.TweetReplySetting
import java.time.Instant

final case class Tweet(id: String,
                       text: String,
                       attachments: Option[TweetAttachments],
                       author_id: Option[String],
                       context_annotations: Option[Seq[TweetContextAnnotation]],
                       conversation_id: Option[String],
                       created_at: Option[Instant],
                       entities: Option[TweetEntities],
                       geo: Option[TweetGeo],
                       in_reply_to_user_id: Option[String],
                       lang: Option[String],
                       non_public_metrics: Option[TweetNonPublicMetrics],
                       organic_metrics: Option[TweetOrganicMetrics],
                       possibly_sensitive: Option[Boolean],
                       promoted_metrics: Option[TweetPromotedMetrics],
                       public_metrics: Option[TweetPublicMetrics],
                       referenced_tweets: Option[Seq[TweetReferencedTweet]],
                       reply_settings: Option[TweetReplySetting],
                       source: Option[String],
                       withheld: Option[Withheld])

final case class TweetAttachments(media_keys: Seq[String], poll_ids: Seq[String])

final case class TweetContextAnnotation(domain: TweetDomain, entity: TweetEntity)

final case class TweetGeo(coordinates: Option[TweetCoordinates], place_id: Option[String])

final case class TweetCoordinates(`type`: CoordinatesType, coordinates: (Double, Double))

final case class TweetNonPublicMetrics(impression_count: Int, url_link_clicks: Option[Int], user_profile_clicks: Int)

final case class TweetOrganicMetrics(impression_count: Int,
                                     url_link_clicks: Option[Int],
                                     user_profile_clicks: Int,
                                     retweet_count: Int,
                                     reply_count: Int,
                                     like_count: Int)

final case class TweetPromotedMetrics(impression_count: Int,
                                      url_link_clicks: Option[Int],
                                      user_profile_clicks: Int,
                                      retweet_count: Int,
                                      reply_count: Int,
                                      like_count: Int)

final case class TweetPublicMetrics(retweet_count: Int, reply_count: Int, like_count: Int, quote_count: Int)

final case class TweetReferencedTweet(`type`: ReferencedTweetType, id: String)

final case class TweetDomain(id: String, name: String, description: Option[String])

final case class TweetEntity(id: String, name: String, description: Option[String])

package com.danielasfregola.twitter4s.entities.v2

final case class Media(media_key: String,
                       `type`: String,
                       duration_ms: Option[Integer],
                       height: Option[Integer],
                       non_public_metrics: Option[MediaNonPublicMetrics],
                       organic_metrics: Option[MediaOrganicMetrics],
                       preview_image_url: Option[String],
                       promoted_metrics: Option[MediaPromotedMetrics],
                       public_metrics: Option[MediaPublicMetrics],
                       width: Option[Integer])

final case class MediaNonPublicMetrics(playback_0_count: Integer,
                                       playback_100_count: Integer,
                                       playback_25_count: Integer,
                                       playback_50_count: Integer,
                                       playback_75_count: Integer)

final case class MediaOrganicMetrics(playback_0_count: Integer,
                                     playback_100_count: Integer,
                                     playback_25_count: Integer,
                                     playback_50_count: Integer,
                                     playback_75_count: Integer,
                                     view_count: Integer)

final case class MediaPromotedMetrics(playback_0_count: Integer,
                                      playback_100_count: Integer,
                                      playback_25_count: Integer,
                                      playback_50_count: Integer,
                                      playback_75_count: Integer,
                                      view_count: Integer)

final case class MediaPublicMetrics(view_count: Integer)

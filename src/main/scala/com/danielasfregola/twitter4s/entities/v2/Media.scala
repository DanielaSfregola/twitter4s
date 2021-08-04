package com.danielasfregola.twitter4s.entities.v2

final case class Media(media_key: String,
                       `type`: String,
                       duration_ms: Option[Int],
                       height: Option[Int],
                       non_public_metrics: Option[MediaNonPublicMetrics],
                       organic_metrics: Option[MediaOrganicMetrics],
                       preview_image_url: Option[String],
                       promoted_metrics: Option[MediaPromotedMetrics],
                       public_metrics: Option[MediaPublicMetrics],
                       width: Option[Int])

final case class MediaNonPublicMetrics(playback_0_count: Int,
                                       playback_100_count: Int,
                                       playback_25_count: Int,
                                       playback_50_count: Int,
                                       playback_75_count: Int)

final case class MediaOrganicMetrics(playback_0_count: Int,
                                     playback_100_count: Int,
                                     playback_25_count: Int,
                                     playback_50_count: Int,
                                     playback_75_count: Int,
                                     view_count: Int)

final case class MediaPromotedMetrics(playback_0_count: Int,
                                      playback_100_count: Int,
                                      playback_25_count: Int,
                                      playback_50_count: Int,
                                      playback_75_count: Int,
                                      view_count: Int)

final case class MediaPublicMetrics(view_count: Int)

package com.danielasfregola.twitter4s.entities

final case class Media(display_url: String,
                       expanded_url: String,
                       id: Long,
                       id_str: String,
                       indices: Seq[Int],
                       media_url: String,
                       media_url_https: String,
                       sizes: Map[String, Size],
                       source_status_id: Option[Long],
                       source_status_id_str: Option[String],
                       `type`: String,
                       url: String,
                       video_info: Option[VideoInfo])

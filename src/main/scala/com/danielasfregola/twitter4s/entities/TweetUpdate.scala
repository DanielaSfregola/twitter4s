package com.danielasfregola.twitter4s.entities

final case class TweetUpdate(status: String,
                             in_reply_to_status_id: Option[Long] = None,
                             possibly_sensitive: Boolean = false,
                             lat: Option[Long] = None,
                             long: Option[Long] = None,
                             place_id: Option[String] = None,
                             display_coordinates: Boolean = false,
                             trim_user: Boolean = false,
                             media_ids: Seq[Long] = Seq.empty)

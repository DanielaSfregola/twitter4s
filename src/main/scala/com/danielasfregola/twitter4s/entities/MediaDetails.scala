package com.danielasfregola.twitter4s.entities

case class MediaDetails(media_id: Long,
                        media_id_string: String,
                        expires_after_secs: Int,
                        size: Long,
                        image: Option[Image] = None,
                        video: Option[Video] = None)

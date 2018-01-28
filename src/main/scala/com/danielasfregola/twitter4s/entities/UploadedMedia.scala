package com.danielasfregola.twitter4s.entities

final case class UploadedMedia(media_id: Long,
                               media_id_str: String,
                               size: Int,
                               image: Option[Image] = None,
                               video: Option[Video] = None)

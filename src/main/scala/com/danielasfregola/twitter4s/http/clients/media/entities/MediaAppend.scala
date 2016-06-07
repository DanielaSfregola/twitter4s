package com.danielasfregola.twitter4s.http.clients.media.entities

case class MediaAppend(media_id: Long,
                       segment_index: Int,
                       media: String,
                       command: String = "APPEND")

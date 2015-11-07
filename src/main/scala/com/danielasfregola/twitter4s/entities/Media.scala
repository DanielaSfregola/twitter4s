package com.danielasfregola.twitter4s.entities

case class Media(display_url: String,
                 expanded_url: String,
                 id: Long,
                 id_str: String,
                 indices: Seq[Int],
                 media_url: String,
                 media_url_https: String,
                 size: Map[String, Size],
                 source_status_id: Long,
                 source_status_id_str: String,
                 `type`: String,
                 url: String)

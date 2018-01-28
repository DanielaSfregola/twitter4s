package com.danielasfregola.twitter4s.entities

final case class Configuration(dm_text_character_limit: Long,
                               characters_reserved_per_media: Int,
                               max_media_per_upload: Int,
                               non_username_paths: Seq[String] = Seq.empty,
                               photo_size_limit: Long,
                               photo_sizes: Map[String, Size] = Map.empty,
                               short_url_length: Int,
                               short_url_length_https: Int)

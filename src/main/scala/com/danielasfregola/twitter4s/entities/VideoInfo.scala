package com.danielasfregola.twitter4s.entities

final case class VideoInfo(aspect_ratio: Seq[Int], duration_millis: Option[Long], variants: Seq[Variant])

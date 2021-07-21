package com.danielasfregola.twitter4s.entities.v2.enums.fields

object MediaFields extends Enumeration {
  type MediaFields = Value

  val DurationMs = Value("duration_ms")
  val Height = Value("height")
  val MediaKey = Value("media_key")
  val PreviewImageUrl = Value("preview_image_url")
  val Type = Value("type")
  val Url = Value("url")
  val Width = Value("width")
  val PublicMetrics = Value("public_metrics")
  val NonPublicMetrics = Value("non_public_metrics")
  val OrganicMetrics = Value("organic_metrics")
  val PromotedMetrics = Value("promoted_metrics")
}

package com.danielasfregola.twitter4s.entities.v2

// https://developer.twitter.com/en/docs/twitter-api/data-dictionary/object-model/place
final case class Place(full_name: String,
                       id: String,
                       contained_within: Seq[String],
                       country: Option[String],
                       country_code: Option[String],
                       geo: Option[GeoJSON],
                       name: Option[String],
                       place_type: Option[String])

final case class GeoJSON(value: String) extends AnyVal

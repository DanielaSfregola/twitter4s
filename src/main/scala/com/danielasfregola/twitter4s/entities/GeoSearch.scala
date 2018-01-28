package com.danielasfregola.twitter4s.entities

final case class GeoSearch(query: GeoQuery, result: GeoResult)

final case class GeoQuery(params: GeoParams, `type`: String, url: String)

final case class GeoResult(places: Seq[GeoPlace] = Seq.empty)

final case class GeoParams(accuracy: String,
                           granularity: String,
                           coordinates: Option[Coordinates] = None,
                           query: Option[String] = None,
                           autocomplete: Boolean = false,
                           trim_place: Boolean = false)

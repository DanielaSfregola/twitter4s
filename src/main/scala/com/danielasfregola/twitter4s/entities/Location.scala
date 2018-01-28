package com.danielasfregola.twitter4s.entities

final case class Location(country: String,
                          countryCode: Option[String],
                          name: String,
                          parentid: Long,
                          placeType: PlaceType,
                          url: String,
                          woeid: Long)

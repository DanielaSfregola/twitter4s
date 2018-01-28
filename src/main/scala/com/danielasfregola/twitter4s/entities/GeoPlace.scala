package com.danielasfregola.twitter4s.entities

final case class GeoPlace(attributes: Map[String, String],
                          bounding_box: Area,
                          country: String,
                          country_code: String,
                          full_name: String,
                          id: String,
                          name: String,
                          place_type: String,
                          url: String,
                          contained_within: Seq[GeoPlace],
                          centroid: Seq[Double] = Seq.empty,
                          polylines: Seq[String] = Seq.empty)

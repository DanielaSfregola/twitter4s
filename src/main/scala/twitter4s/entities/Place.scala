package twitter4s.entities

import spray.http.Uri

case class Place(
                attributes: Map[String, String],
                bounding_box: Area,
                country: String,
                country_code: String,
                full_name: String,
                id: String,
                name: String,
                place_type: String,
                url: Uri,
                contained_within: Seq[Place]
                )


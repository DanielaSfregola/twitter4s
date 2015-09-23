package twitter4s.entities

case class Place(
                id: String,
                url: String, // TODO - check if any issues with escape chars and maybe make it into URI?
                place_type: String,
                name: String,
                full_name: String,
                country_code: String,
                country: String,
                contained_within: Seq[String], // TODO, it was empty
                bounding_box: Area,
                attributes: String // TODO - it was empty obj
                )


package twitter4s.entities

case class Place(attributes: Map[String, String],
                 bounding_box: Area,
                 country: String,
                 country_code: String,
                 full_name: String,
                 id: String,
                 name: String,
                 place_type: String,
                 url: String,
                 contained_within: Seq[Place])


package twitter4s.entities

case class GeoSearch(query: GeoQuery, result: GeoResult)

case class GeoQuery(params: GeoParams, `type`: String, url: String)

case class GeoResult(places: Seq[GeoPlace] = Seq.empty)

case class GeoParams(accuracy: String, coordinates: Coordinates, granularity: String)


package twitter4s.entities

case class LocationTrends(as_of: String, // TODO - convert to Date? Different date format here...
                          created_at: String, // TODO - convert to Date? Different date format here...
                          locations: Seq[LocationOverview] = Seq.empty,
                          trends: Seq[Trend] = Seq.empty)

case class LocationOverview(name: String, woeid: Long)

// TODO - support 'promoted_content' and 'events'
case class Trend(name: String, query: String, url: String)

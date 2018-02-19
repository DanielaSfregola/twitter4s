package com.danielasfregola.twitter4s.entities

final case class LocationTrends(as_of: String, // TODO - convert to Instant? Different date format here...
                                created_at: String, // TODO - convert to Instant? Different date format here...
                                locations: Seq[LocationOverview] = Seq.empty,
                                trends: Seq[Trend] = Seq.empty)

final case class LocationOverview(name: String, woeid: Long)

// TODO - support 'promoted_content' and 'events'
final case class Trend(name: String, query: String, url: String, tweet_volume: Option[Long])

package com.danielasfregola.twitter4s.entities

final case class StatusSearch(statuses: List[Tweet], search_metadata: SearchMetadata)

final case class SearchMetadata(completed_in: Double,
                                max_id: Long,
                                max_id_str: String,
                                next_results: Option[String],
                                query: String,
                                refresh_url: Option[String],
                                count: Int,
                                since_id: Long,
                                since_id_str: String)

final case class StatusMetadata(iso_language_code: String, result_type: String)

final case class StatusFullSearch(data: List[MinimalTweet], meta: FullSearchMetadata)

final case class FullSearchMetadata(newest_id: Option[String],
                                    oldest_id: Option[String],
                                    result_count: Int,
                                    next_token: Option[String])

// TODO: break this out into entities?
// Full archive search only returns this when there are tweets returned
final case class MinimalTweet(id: String, text: String)

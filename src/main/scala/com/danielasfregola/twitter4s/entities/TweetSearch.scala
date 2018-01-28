package com.danielasfregola.twitter4s.entities

final case class StatusSearch(statuses: List[Tweet], search_metadata: SearchMetadata)

final case class SearchMetadata(completed_in: Double,
                                max_id: Long,
                                max_id_str: String,
                                next_results: Option[String],
                                query: String,
                                refresh_url: String,
                                count: Int,
                                since_id: Long,
                                since_id_str: String)

final case class StatusMetadata(iso_language_code: String, result_type: String)

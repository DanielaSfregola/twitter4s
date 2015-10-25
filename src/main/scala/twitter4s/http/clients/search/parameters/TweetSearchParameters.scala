package twitter4s.http.clients.search.parameters

import java.time.LocalDate

import twitter4s.entities.GeoCode
import twitter4s.entities.enums.Language.Language
import twitter4s.entities.enums.ResultType.ResultType
import twitter4s.http.marshalling.Parameters

case class TweetSearchParameters(q: String,
                                 count: Int,
                                 include_entities: Boolean,
                                 result_type: ResultType,
                                 geocode: Option[GeoCode],
                                 lang : Option[Language],
                                 locale: Option[String],
                                 until: Option[LocalDate],
                                 since_id: Option[Long],
                                 max_id: Option[Long],
                                 callback: Option[String]) extends Parameters

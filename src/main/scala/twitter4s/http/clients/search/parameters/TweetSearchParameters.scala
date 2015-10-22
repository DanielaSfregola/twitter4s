package twitter4s.http.clients.search.parameters

import java.time.LocalDate

import twitter4s.entities.GeoCode
import twitter4s.entities.enums.Language.Language
import twitter4s.entities.enums.ResultType
import twitter4s.entities.enums.ResultType.ResultType
import twitter4s.http.marshalling.Parameters

case class TweetSearchParameters(q: String,
                                 count: Int = 15,
                                 include_entities: Boolean = true,
                                 result_type: ResultType = ResultType.Mixed,
                                 geocode: Option[GeoCode],
                                 lang : Option[Language],
                                 locale: Option[String],
                                 until: Option[LocalDate],
                                 since_id: Option[Long],
                                 max_id: Option[Long],
                                 callback: Option[String]) extends Parameters

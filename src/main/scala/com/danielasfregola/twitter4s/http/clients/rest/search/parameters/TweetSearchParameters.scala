package com.danielasfregola.twitter4s.http.clients.rest.search.parameters

import java.time.LocalDate

import com.danielasfregola.twitter4s.entities.GeoCode
import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.enums.ResultType.ResultType
import com.danielasfregola.twitter4s.entities.enums.TweetMode.TweetMode
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class TweetSearchParameters(q: String,
                                                          count: Int,
                                                          include_entities: Boolean,
                                                          result_type: ResultType,
                                                          geocode: Option[GeoCode],
                                                          lang: Option[Language],
                                                          locale: Option[String],
                                                          until: Option[LocalDate],
                                                          since_id: Option[Long],
                                                          max_id: Option[Long],
                                                          callback: Option[String],
                                                          tweet_mode: TweetMode)
    extends Parameters

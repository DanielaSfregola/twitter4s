package com.danielasfregola.twitter4s.http.clients.rest.search.parameters

import java.time.LocalDate
import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class TweetSearchAllParamaters(q: String,
                                    max_results: Int = 10,
                                    next_token: Option[String],
                                    start_time: Option[LocalDate],
                                    end_time: Option[LocalDate])
    extends Parameters

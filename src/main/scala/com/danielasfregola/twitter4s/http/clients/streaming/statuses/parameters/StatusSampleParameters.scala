package com.danielasfregola.twitter4s.http.clients.streaming.statuses.parameters

import com.danielasfregola.twitter4s.entities.enums.FilterLevel.FilterLevel
import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class StatusSampleParameters(language: Seq[Language],
                                                           stall_warnings: Boolean,
                                                           tracks: Seq[String],
                                                           filter_level: FilterLevel)
    extends Parameters

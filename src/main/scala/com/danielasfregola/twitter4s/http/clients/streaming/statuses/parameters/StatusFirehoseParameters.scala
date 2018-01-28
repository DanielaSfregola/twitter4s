package com.danielasfregola.twitter4s.http.clients.streaming.statuses.parameters

import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class StatusFirehoseParameters(language: Seq[Language],
                                                             count: Option[Int],
                                                             stall_warnings: Boolean)
    extends Parameters

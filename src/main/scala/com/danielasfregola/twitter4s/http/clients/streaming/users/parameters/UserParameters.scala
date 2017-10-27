package com.danielasfregola.twitter4s.http.clients.streaming.users.parameters

import com.danielasfregola.twitter4s.entities.enums.FilterLevel.FilterLevel
import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.enums.WithFilter.WithFilter
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class UserParameters(`with`: WithFilter,
                                                   replies: Option[String],
                                                   track: Seq[String],
                                                   locations: Seq[(Double, Double)],
                                                   stringify_friend_ids: Boolean,
                                                   language: Seq[Language],
                                                   stall_warnings: Boolean,
                                                   filter_level: FilterLevel)
    extends Parameters

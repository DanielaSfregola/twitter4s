package com.danielasfregola.twitter4s.http.clients.streaming.users.parameters

import com.danielasfregola.twitter4s.entities.enums.WithFilter.WithFilter
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] case class UserParameters(`with`: WithFilter,
                                             replies: Option[String],
                                             track: Seq[String],
                                             locations: Seq[Double],
                                             stringify_friend_ids: Boolean,
                                             stall_warnings: Boolean) extends Parameters

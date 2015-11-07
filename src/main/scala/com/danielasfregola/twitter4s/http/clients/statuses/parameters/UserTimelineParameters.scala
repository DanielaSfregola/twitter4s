package com.danielasfregola.twitter4s.http.clients.statuses.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class UserTimelineParameters(user_id: Option[Long],
                                  screen_name: Option[String],
                                  since_id: Option[Long],
                                  count: Int,
                                  max_id: Option[Long],
                                  trim_user: Boolean,
                                  exclude_replies: Boolean,
                                  contributor_details: Boolean,
                                  include_rts: Boolean) extends Parameters










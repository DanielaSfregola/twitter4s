package com.danielasfregola.twitter4s.http.clients.rest.lists.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class MembershipsParameters(user_id: Option[Long],
                                                          screen_name: Option[String],
                                                          count: Int,
                                                          cursor: Long,
                                                          filter_to_owned_lists: Boolean)
    extends Parameters

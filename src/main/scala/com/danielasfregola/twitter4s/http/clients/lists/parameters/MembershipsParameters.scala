package com.danielasfregola.twitter4s.http.clients.lists.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class MembershipsParameters(user_id: Option[Long],
                                 screen_name: Option[String],
                                 count: Int,
                                 cursor: Long,
                                 filter_to_owned_lists: Boolean) extends Parameters

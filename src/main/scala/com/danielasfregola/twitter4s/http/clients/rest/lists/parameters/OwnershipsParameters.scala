package com.danielasfregola.twitter4s.http.clients.rest.lists.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class OwnershipsParameters(user_id: Option[Long],
                                 screen_name: Option[String],
                                 count: Int,
                                 cursor: Long) extends Parameters

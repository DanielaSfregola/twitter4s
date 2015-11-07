package com.danielasfregola.twitter4s.http.clients.friends.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class FriendsParameters(user_id: Option[Long],
                             screen_name: Option[String],
                             cursor: Long,
                             count: Int,
                             skip_status: Boolean,
                             include_user_entities: Boolean) extends Parameters

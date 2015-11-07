package com.danielasfregola.twitter4s.http.clients.friends.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class FriendParameters(user_id: Option[Long],
                                screen_name: Option[String],
                                cursor: Long,
                                count: Int,
                                stringify_ids: Boolean) extends Parameters

package com.danielasfregola.twitter4s.http.clients.rest.friends.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class FriendParameters(user_id: Option[Long],
                                                     screen_name: Option[String],
                                                     cursor: Long,
                                                     count: Int,
                                                     stringify_ids: Boolean)
    extends Parameters

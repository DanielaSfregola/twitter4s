package com.danielasfregola.twitter4s.http.clients.rest.followers.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] case class FollowingParameters(user_id: Option[Long],
                                                  screen_name: Option[String],
                                                  cursor: Long,
                                                  count: Int,
                                                  stringify_ids: Boolean) extends Parameters

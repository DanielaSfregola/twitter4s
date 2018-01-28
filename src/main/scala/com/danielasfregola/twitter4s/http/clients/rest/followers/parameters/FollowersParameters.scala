package com.danielasfregola.twitter4s.http.clients.rest.followers.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class FollowersParameters(user_id: Option[Long],
                                                        screen_name: Option[String],
                                                        cursor: Long,
                                                        count: Int,
                                                        skip_status: Boolean,
                                                        include_user_entities: Boolean)
    extends Parameters

package com.danielasfregola.twitter4s.http.clients.rest.users.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class UsersParameters(user_id: Option[String],
                                                    screen_name: Option[String],
                                                    include_entities: Boolean)
    extends Parameters

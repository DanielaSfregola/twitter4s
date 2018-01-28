package com.danielasfregola.twitter4s.http.clients.rest.users.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class UserParameters(user_id: Option[Long],
                                                   screen_name: Option[String],
                                                   include_entities: Boolean)
    extends Parameters

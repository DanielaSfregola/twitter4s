package com.danielasfregola.twitter4s.http.clients.rest.lists.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class ListsParameters(user_id: Option[Long],
                                                    screen_name: Option[String],
                                                    reverse: Boolean)
    extends Parameters

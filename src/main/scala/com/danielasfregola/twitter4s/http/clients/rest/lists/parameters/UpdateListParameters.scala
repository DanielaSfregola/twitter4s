package com.danielasfregola.twitter4s.http.clients.rest.lists.parameters

import com.danielasfregola.twitter4s.entities.enums.Mode.Mode
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class UpdateListParameters(list_id: Option[Long],
                                                         slug: Option[String],
                                                         owner_screen_name: Option[String],
                                                         owner_id: Option[Long],
                                                         description: Option[String],
                                                         name: Option[String],
                                                         mode: Option[Mode])
    extends Parameters

package com.danielasfregola.twitter4s.http.clients.rest.lists.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class ListParameters(list_id: Option[Long] = None,
                                                   slug: Option[String] = None,
                                                   owner_screen_name: Option[String] = None,
                                                   owner_id: Option[Long] = None)
    extends Parameters

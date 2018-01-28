package com.danielasfregola.twitter4s.http.clients.rest.lists.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class SubscribersParameters(list_id: Option[Long],
                                                          slug: Option[String],
                                                          owner_screen_name: Option[String],
                                                          owner_id: Option[Long],
                                                          count: Int,
                                                          cursor: Long,
                                                          include_entities: Boolean,
                                                          skip_status: Boolean)
    extends Parameters

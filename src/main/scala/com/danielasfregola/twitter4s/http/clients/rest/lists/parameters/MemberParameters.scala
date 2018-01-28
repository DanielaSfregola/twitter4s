package com.danielasfregola.twitter4s.http.clients.rest.lists.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class MemberParameters(list_id: Option[Long] = None,
                                                     slug: Option[String] = None,
                                                     user_id: Option[Long] = None,
                                                     screen_name: Option[String] = None,
                                                     owner_screen_name: Option[String] = None,
                                                     owner_id: Option[Long] = None,
                                                     include_entities: Boolean,
                                                     skip_status: Boolean)
    extends Parameters

package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] case class MentionsParameters(count: Int,
                                                 since_id: Option[Long],
                                                 max_id: Option[Long],
                                                 trim_user: Boolean,
                                                 contributor_details: Boolean,
                                                 include_entities: Boolean) extends Parameters

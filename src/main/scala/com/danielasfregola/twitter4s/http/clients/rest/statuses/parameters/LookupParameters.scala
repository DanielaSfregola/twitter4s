package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class LookupParameters(id: String,
                                               include_entities: Boolean,
                                               trim_user: Boolean,
                                               map: Boolean) extends Parameters

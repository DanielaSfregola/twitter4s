package com.danielasfregola.twitter4s.http.clients.rest.directmessages.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class ReceivedParameters(since_id: Option[Long],
                              max_id: Option[Long],
                              count: Int,
                              include_entities: Boolean,
                              skip_status: Boolean) extends Parameters

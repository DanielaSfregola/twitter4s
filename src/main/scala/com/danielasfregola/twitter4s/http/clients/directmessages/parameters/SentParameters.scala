package com.danielasfregola.twitter4s.http.clients.directmessages.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class SentParameters(since_id: Option[Long],
                          max_id: Option[Long],
                          count: Int,
                          include_entities: Boolean,
                          page: Int) extends Parameters

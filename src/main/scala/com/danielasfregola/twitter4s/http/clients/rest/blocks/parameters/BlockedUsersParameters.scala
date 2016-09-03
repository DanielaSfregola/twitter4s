package com.danielasfregola.twitter4s.http.clients.rest.blocks.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class BlockedUsersParameters(include_entities: Boolean,
                                  skip_status: Boolean,
                                  cursor: Long) extends Parameters

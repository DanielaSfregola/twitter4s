package com.danielasfregola.twitter4s.http.clients.blocks.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class BlockedUserIdsParameters(stringify_ids: Boolean, cursor: Long) extends Parameters

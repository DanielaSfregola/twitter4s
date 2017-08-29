package com.danielasfregola.twitter4s.http.clients.rest.blocks.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class BlockedUserIdsParameters(stringify_ids: Boolean, cursor: Long) extends Parameters

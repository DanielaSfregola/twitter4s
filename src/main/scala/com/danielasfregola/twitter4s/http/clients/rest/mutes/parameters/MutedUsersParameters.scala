package com.danielasfregola.twitter4s.http.clients.rest.mutes.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class MutedUsersParameters(cursor: Long, include_entities: Boolean, skip_status: Boolean)
    extends Parameters

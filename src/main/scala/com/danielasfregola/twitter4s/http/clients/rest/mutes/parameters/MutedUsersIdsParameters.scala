package com.danielasfregola.twitter4s.http.clients.rest.mutes.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class MutedUsersIdsParameters(cursor: Long) extends Parameters

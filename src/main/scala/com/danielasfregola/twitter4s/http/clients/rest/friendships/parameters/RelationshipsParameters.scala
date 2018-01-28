package com.danielasfregola.twitter4s.http.clients.rest.friendships.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class RelationshipsParameters(user_id: Option[String], screen_name: Option[String])
    extends Parameters

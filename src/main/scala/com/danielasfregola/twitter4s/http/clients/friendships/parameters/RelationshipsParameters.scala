package com.danielasfregola.twitter4s.http.clients.friendships.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class RelationshipsParameters(user_id: Option[String],
                                   screen_name: Option[String]) extends Parameters

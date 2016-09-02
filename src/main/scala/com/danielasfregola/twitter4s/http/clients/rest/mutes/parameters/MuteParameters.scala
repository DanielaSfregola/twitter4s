package com.danielasfregola.twitter4s.http.clients.rest.mutes.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class MuteParameters(user_id: Option[Long], screen_name: Option[String]) extends Parameters

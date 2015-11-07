package com.danielasfregola.twitter4s.http.clients.users.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class BannersParameters(user_id: Option[Long],
                          screen_name: Option[String]) extends Parameters

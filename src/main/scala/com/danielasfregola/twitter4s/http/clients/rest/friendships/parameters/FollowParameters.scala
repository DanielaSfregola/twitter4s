package com.danielasfregola.twitter4s.http.clients.rest.friendships.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class FollowParameters(user_id: Option[Long],
                            screen_name: Option[String],
                            follow: Boolean) extends Parameters

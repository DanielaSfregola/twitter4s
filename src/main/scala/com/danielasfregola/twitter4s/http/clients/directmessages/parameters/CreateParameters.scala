package com.danielasfregola.twitter4s.http.clients.directmessages.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class CreateParameters(user_id: Option[Long] = None,
                            screen_name: Option[String] = None,
                            text: String) extends Parameters

package com.danielasfregola.twitter4s.http.clients.lists.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class AddMemberParameters(list_id: Option[Long] = None,
                               slug: Option[String] = None,
                               user_id: Option[Long] = None,
                               screen_name: Option[String] = None,
                               owner_screen_name: Option[String] = None,
                               owner_id: Option[Long] = None) extends Parameters

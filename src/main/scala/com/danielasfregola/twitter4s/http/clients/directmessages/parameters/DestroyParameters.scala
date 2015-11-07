package com.danielasfregola.twitter4s.http.clients.directmessages.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class DestroyParameters(id: Long, include_entities: Boolean) extends Parameters

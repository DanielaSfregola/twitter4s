package com.danielasfregola.twitter4s.http.clients.rest.lists.parameters

import com.danielasfregola.twitter4s.entities.enums.Mode.Mode
import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class CreateListParameters(name: String,
                                mode: Mode,
                                description: Option[String]) extends Parameters

package com.danielasfregola.twitter4s.http.clients.rest.directmessages.parameters
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class EventListParameters(count: Int, next_cursor: Option[String]) extends Parameters

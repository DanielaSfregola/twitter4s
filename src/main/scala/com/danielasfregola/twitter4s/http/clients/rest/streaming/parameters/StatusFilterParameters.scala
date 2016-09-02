package com.danielasfregola.twitter4s.http.clients.rest.streaming.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class StatusFilterParameters(follow: Option[String] = None,
                                  track: Option[String] = None,
                                  locations: Option[String] = None,
                                  stall_warnings: Boolean = false) extends Parameters{
  require(follow.orElse(track).orElse(locations).isDefined,
    "At least one of 'follow', 'track' or 'locations' needs to be defined")
}

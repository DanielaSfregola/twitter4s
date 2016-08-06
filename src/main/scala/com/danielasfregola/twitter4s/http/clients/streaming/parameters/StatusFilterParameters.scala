package com.danielasfregola.twitter4s.http.clients.streaming.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

/**
 * Created by gerardo.mendez on 8/3/16.
 */

case class StatusFilterParameters(follow: Option[String] = None,
                                  track: Option[String] = None,
                                  locations: Option[String] = None) extends Parameters{
  require(follow.orElse(track).orElse(locations).isDefined,
    "At least one of 'follow', 'track' or 'locations' needs to be defined")
}
package com.danielasfregola.twitter4s.http.clients.streaming.statuses.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class StatusFilterParameters(follow: Option[String] = None,
                                  track: Option[String] = None,
                                  locations: Option[String] = None,
                                  stall_warnings: Boolean = false) extends Parameters

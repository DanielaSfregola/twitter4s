package com.danielasfregola.twitter4s.http.clients.streaming.statuses.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] case class StatusFilterParameters(follow: Seq[Long],
                                                     track: Seq[String],
                                                     locations: Seq[Double],
                                                     stall_warnings: Boolean) extends Parameters

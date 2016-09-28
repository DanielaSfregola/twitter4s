package com.danielasfregola.twitter4s.http.clients.streaming.statuses.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] case class StatusFirehoseParameters(count: Option[Int],
                                                       stall_warnings: Boolean) extends Parameters

package com.danielasfregola.twitter4s.http.clients.streaming.statuses.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class StatusSampleParameters(stall_warnings: Boolean = false) extends Parameters

package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class RetweetsParameters(count: Int, trim_user: Boolean) extends Parameters

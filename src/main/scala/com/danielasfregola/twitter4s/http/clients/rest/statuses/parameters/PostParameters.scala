package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] case class PostParameters(trim_user: Boolean) extends Parameters

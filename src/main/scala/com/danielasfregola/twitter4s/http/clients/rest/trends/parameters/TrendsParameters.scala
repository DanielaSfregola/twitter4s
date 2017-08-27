package com.danielasfregola.twitter4s.http.clients.rest.trends.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class TrendsParameters(id: Long, exclude: Option[String]) extends Parameters

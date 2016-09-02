package com.danielasfregola.twitter4s.http.clients.rest.application.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class RatesParameters(resources: Option[String]) extends Parameters

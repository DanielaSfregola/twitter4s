package twitter4s.http.clients.application.parameters

import twitter4s.http.marshalling.Parameters

case class RatesParameters(resources: Option[String]) extends Parameters

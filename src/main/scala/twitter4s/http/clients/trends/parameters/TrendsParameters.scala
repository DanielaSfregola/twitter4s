package twitter4s.http.clients.trends.parameters

import twitter4s.http.marshalling.Parameters

case class TrendsParameters(id: Long, exclude: Option[String] = None) extends Parameters

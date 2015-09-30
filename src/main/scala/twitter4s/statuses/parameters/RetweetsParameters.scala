package twitter4s.statuses.parameters

import twitter4s.http.marshalling.Parameters

case class RetweetsParameters(count: Int, trim_user: Boolean) extends Parameters

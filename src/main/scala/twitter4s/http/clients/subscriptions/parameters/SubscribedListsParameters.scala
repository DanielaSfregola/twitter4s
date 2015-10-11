package twitter4s.http.clients.subscriptions.parameters

import twitter4s.http.marshalling.Parameters

case class SubscribedListsParameters(user_id: Option[Long],
                                     screen_name: Option[String],
                                     reverse: Boolean) extends Parameters

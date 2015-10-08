package twitter4s.http.clients.friendships.parameters

import twitter4s.http.marshalling.Parameters

abstract class NotificationParameters extends Parameters

case class RetweetNotificationParameters(user_id: Option[Long],
                                         screen_name: Option[String],
                                         retweets: Boolean) extends NotificationParameters

case class DeviceNotificationParameters(user_id: Option[Long],
                                         screen_name: Option[String],
                                         device: Boolean) extends NotificationParameters

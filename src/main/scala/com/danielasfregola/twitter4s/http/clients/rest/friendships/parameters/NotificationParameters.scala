package com.danielasfregola.twitter4s.http.clients.rest.friendships.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

abstract class NotificationParameters extends Parameters

private[twitter4s] final case class RetweetNotificationParameters(user_id: Option[Long],
                                                                  screen_name: Option[String],
                                                                  retweets: Boolean)
    extends NotificationParameters

private[twitter4s] final case class DeviceNotificationParameters(user_id: Option[Long],
                                                                 screen_name: Option[String],
                                                                 device: Boolean)
    extends NotificationParameters

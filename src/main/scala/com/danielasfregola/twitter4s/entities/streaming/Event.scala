package com.danielasfregola.twitter4s.entities.streaming

import java.util.Date

import com.danielasfregola.twitter4s.entities.User
import com.danielasfregola.twitter4s.entities.enums.EventCode.EventCode

/** Everytime a user updates their profile we broadcast a user_update event to indicate
  * that an update has been made to the user profile.
  * The source and target objects are identical in content.
  * For more information see
  * <a href="https://dev.twitter.com/streaming/overview/messages-types#user_update" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#user_update</a>.
  */
case class Event(created_at: Date,
                 event: EventCode,
                 target: User,
                 source: User,
                 target_object: Option[EventTargetObject]) extends StreamingMessage

trait EventTargetObject


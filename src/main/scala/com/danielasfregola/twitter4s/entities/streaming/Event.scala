package com.danielasfregola.twitter4s.entities.streaming

import java.util.Date

import com.danielasfregola.twitter4s.entities.User
import com.danielasfregola.twitter4s.entities.enums.EventCode.EventCode

case class Event(created_at: Date,
                 event: EventCode,
                 target: User,
                 source: User,
                 target_object: Option[EventTargetObject]) extends StreamingMessage

trait EventTargetObject

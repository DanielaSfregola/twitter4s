package com.danielasfregola.twitter4s.entities
import com.danielasfregola.twitter4s.entities.streaming.UserStreamingMessage

final case class EventList(events: List[Event],
                           apps: Option[Map[String, Apps]],
                           next_cursor: Option[String]) extends UserStreamingMessage

final case class Apps(id: String, name: String, url: String)

package com.danielasfregola.twitter4s.entities
import com.danielasfregola.twitter4s.entities.streaming.UserStreamingMessage

final case class DirectMessageEventList(events: List[DirectMessageEvent],
                                        apps: Option[Map[String, Apps]],
                                        next_cursor: Option[String])
    extends UserStreamingMessage

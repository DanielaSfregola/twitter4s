package com.danielasfregola.twitter4s.entities

import java.time.Instant
import com.danielasfregola.twitter4s.entities.streaming.UserStreamingMessage

final case class DirectMessageEvent(`type`: String,
                                    id: Long,
                                    created_timestamp: Instant,
                                    recipient_id: Long,
                                    sender_id: Long,
                                    text: String,
                                    entities: Option[Entities2])
    extends UserStreamingMessage

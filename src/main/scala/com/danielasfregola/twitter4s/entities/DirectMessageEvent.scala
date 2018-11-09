package com.danielasfregola.twitter4s.entities
import java.time.LocalDateTime

import com.danielasfregola.twitter4s.entities.enums.TweetType

final case class DirectMessageEvent(`type`: TweetType.Value = TweetType.messageCreate,
                                    id: DirectMessageId,
                                    created_timestamp: LocalDateTime,
                                    message_create: MessageCreate)

final case class MessageCreate(target: Target, sender_id: Option[String], message_data: MessageData)

final case class Target(recipient_id: String)

final case class MessageData(text: String, entities: Option[Entities] = None, attachment: Option[Attachment] = None)

final case class Attachment(`type`: String, media: Media)

final case class Apps(id: String, name: String, url: String)

final case class SingleEvent(event: DirectMessageEvent, apps: Map[String, App] = Map.empty)

final case class DirectMessageId(id: Long)
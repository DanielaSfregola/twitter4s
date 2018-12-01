package com.danielasfregola.twitter4s.entities
import java.time.Instant

final case class DirectMessageEvent(`type`: String,
                                    id: String,
                                    created_timestamp: Instant,
                                    message_create: MessageCreate)

final case class MessageCreate(target: Target, sender_id: Option[String], message_data: MessageData)

final case class Target(recipient_id: String)

final case class MessageData(text: String, entities: Option[Entities] = None, attachment: Option[Attachment] = None)

final case class Attachment(`type`: String, media: Media)

final case class Apps(id: String, name: String, url: String)

final case class Event(event: DirectMessageEvent, apps: Map[String, App] = Map.empty)

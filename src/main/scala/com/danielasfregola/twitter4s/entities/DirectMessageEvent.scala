package com.danielasfregola.twitter4s.entities

final case class DirectMessageEvent(`type`: String,
                                    id: String,
                                    created_timestamp: String,
                                    message_create: MessageCreate)

final case class MessageCreate(target: Target, sender_id: Option[String], message_data: MessageData)

final case class Target(recipient_id: String)

final case class MessageData(text: String, entities: Option[Entities] = None, attachment: Option[Attachment])

final case class Attachment(`type`: String, media: Media)

final case class Apps(id: String, name: String, url: String)

final case class SingleEvent(event: DirectMessageEvent, apps: Option[Map[String, App]])

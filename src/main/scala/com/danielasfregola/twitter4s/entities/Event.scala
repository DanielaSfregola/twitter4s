package com.danielasfregola.twitter4s.entities

final case class Event(`type`: String, id: Long, created_timestamp: String, message_create: MessageCreate)

final case class MessageCreate(target: Target, sender_id: Option[Long], message_data: MessageData)

final case class Target(recipient_id: Long)

final case class MessageData(text: String, entities: Option[Entities] = None)

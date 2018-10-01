package com.danielasfregola.twitter4s.entities

private[twitter4s] final case class NewDirectMessageEvent(event: NewEvent)

private[twitter4s] final case class NewEvent(`type`: String = "message_create", message_create: MessageCreate)

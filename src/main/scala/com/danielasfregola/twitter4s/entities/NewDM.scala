package com.danielasfregola.twitter4s.entities

private[twitter4s] final case class NewDM (event: NewEvent)

private[twitter4s] final case class NewEvent(`type`: String = "message_create", message_create: MessageCreate)

package com.danielasfregola.twitter4s.entities
import com.danielasfregola.twitter4s.entities.enums.TweetType

private[twitter4s] final case class NewDirectMessageEvent(event: NewEvent)

private[twitter4s] final case class NewEvent(`type`: TweetType.Value = TweetType.messageCreate,
                                             message_create: MessageCreate)

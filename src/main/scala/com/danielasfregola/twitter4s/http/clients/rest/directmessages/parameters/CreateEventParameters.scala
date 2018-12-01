package com.danielasfregola.twitter4s.http.clients.rest.directmessages.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class CreateEventParameters(event: CreateDirectMessageEventParameters) extends Parameters

private[twitter4s] object CreateEventParameters {

  def apply(recipient_id: String, text: String): CreateEventParameters = {
    val target = Target(recipient_id)
    val messageData = MessageData(text)
    val messageCreate = MessageCreateParameter(target, messageData)
    val event = CreateDirectMessageEventParameters(messageCreate)
    apply(event)
  }
}

private[twitter4s] final case class CreateDirectMessageEventParameters(message_create: MessageCreateParameter,
                                                                       `type`: String = "message_create")

private[twitter4s] final case class MessageCreateParameter(target: Target, message_data: MessageData)

private[twitter4s] final case class Target(recipient_id: String)

private[twitter4s] final case class MessageData(text: String)

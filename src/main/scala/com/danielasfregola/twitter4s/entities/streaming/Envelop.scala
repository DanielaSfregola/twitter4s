package com.danielasfregola.twitter4s.entities.streaming

case class Envelop(for_user: Long, message: StreamingMessage) extends StreamingMessage

case class EnvelopStringified(for_user: String, message: StreamingMessage) extends StreamingMessage

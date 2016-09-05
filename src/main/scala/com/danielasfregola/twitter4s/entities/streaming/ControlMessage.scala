package com.danielasfregola.twitter4s.entities.streaming

case class ControlMessage(control: ControlMessageInfo) extends StreamingMessage

case class ControlMessageInfo(control_uri: String)

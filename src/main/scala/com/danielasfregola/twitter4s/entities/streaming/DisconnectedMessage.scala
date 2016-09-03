package com.danielasfregola.twitter4s.entities.streaming

import com.danielasfregola.twitter4s.entities.enums.DisconnectionCode.DisconnectionCode

case class DisconnectedMessage(disconnect: DisconnectedMessageInfo) extends StreamingMessage

case class DisconnectedMessageInfo(code: DisconnectionCode,
                                   stream_name: String,
                                   reason: String)

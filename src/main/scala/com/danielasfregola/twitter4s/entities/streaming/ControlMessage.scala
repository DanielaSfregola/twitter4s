package com.danielasfregola.twitter4s.entities.streaming

/** New Site Streams connections will receive a control message which may be used to modify the Site Streams connection without reconnecting.
  * See <a href="https://dev.twitter.com/streaming/sitestreams/controlstreams" target="_blank">Control Streams for Site Streams</a> for details. Note that this message will not necessarily be the first message delivered on a Site Streams connection.
  * For more information see <a href="https://dev.twitter.com/streaming/overview/messages-types#control_messages_control" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#control_messages_control</a>
  */
case class ControlMessage(control: ControlMessageInfo) extends StreamingMessage

case class ControlMessageInfo(control_uri: String)

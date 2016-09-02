package com.danielasfregola.twitter4s.entities.streaming

case class LimitNotice(limit: LimitTrack) extends StreamingMessage

case class LimitTrack(track: Long)

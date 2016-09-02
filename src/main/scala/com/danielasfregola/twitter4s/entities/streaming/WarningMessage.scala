package com.danielasfregola.twitter4s.entities.streaming

import java.util.Date

import com.danielasfregola.twitter4s.entities.User
import com.danielasfregola.twitter4s.entities.enums.DisconnectionCode.DisconnectionCode
import com.danielasfregola.twitter4s.entities.enums.EventCode.EventCode

case class WarningMessage(warning: WarningMessageInfo) extends StreamingMessage

case class WarningMessageInfo(code: String,
                              message: String,
                              percent_full: Option[Int] = None,
                              user_id: Option[Long] = None)

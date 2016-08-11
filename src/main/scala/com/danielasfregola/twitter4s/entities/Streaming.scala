package com.danielasfregola.twitter4s.entities

import java.util.Date

import com.danielasfregola.twitter4s.entities.enums.DisconnectionCode.DisconnectionCode
import com.danielasfregola.twitter4s.entities.enums.EventCode.EventCode

/**
 * Created by gerardo.mendez on 8/8/16.
 */

case class StreamingUpdate(streamingEvent: StreamingMessage)

trait StreamingMessage

case class StatusDeletionNotice(delete: StatusDeletionId) extends StreamingMessage

case class StatusDeletionId(id: Long,
                            id_str: String,
                            user_id: Long,
                            user_id_str: String)

case class LocationDeletionNotice(scrub_geo: LocationDeletionId) extends StreamingMessage

case class LocationDeletionId(user_id: Long,
                              user_id_str: String,
                              up_to_status_id: Long,
                              up_to_status_id_str: String)

case class LimitNotice(limit: LimitTrack) extends StreamingMessage

case class LimitTrack(track: Long)


case class StatusWithheldNotice(status_withheld: StatusWithheldId) extends StreamingMessage

case class StatusWithheldId(id: Long,
                            id_str: String,
                            user_id: Long,
                            user_id_str: String,
                            withheld_in_countries: List[String])

case class UserWithheldNotice(status_withheld: UserWithheldId) extends StreamingMessage

case class UserWithheldId(id: Long,
                          id_str: String,
                          user_id: Long,
                          user_id_str: String,
                          withheld_in_countries: List[String])

case class DisconnectedMessage(disconnect: DisconnectMessageInfo) extends StreamingMessage

case class DisconnectMessageInfo(code: DisconnectionCode,
                                 stream_name: String,
                                 reason: String)

case class WarningMessage(warning: WarningMessageInfo) extends StreamingMessage

case class WarningMessageInfo(code: String,
                              message: String,
                              percent_full: Option[Int] = None,
                              user_id: Option[Long] = None)

trait EventTargetObject

case class Event(created_at: Date,
                 event: EventCode,
                 target: User,
                 source: User,
                 target_object: Option[EventTargetObject]) extends StreamingMessage

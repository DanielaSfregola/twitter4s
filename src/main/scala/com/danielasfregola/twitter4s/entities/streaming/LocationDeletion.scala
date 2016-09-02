package com.danielasfregola.twitter4s.entities.streaming

case class LocationDeletionNotice(scrub_geo: LocationDeletionId) extends StreamingMessage

case class LocationDeletionId(user_id: Long,
                              user_id_str: String,
                              up_to_status_id: Long,
                              up_to_status_id_str: String)

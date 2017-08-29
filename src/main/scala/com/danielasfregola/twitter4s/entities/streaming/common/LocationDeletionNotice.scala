package com.danielasfregola.twitter4s.entities.streaming.common

import com.danielasfregola.twitter4s.entities.streaming.CommonStreamingMessage

/** These messages indicate that geolocated data must be stripped from a range of Tweets.
  * Clients must honor these messages by deleting geocoded data from Tweets which fall before
  * the given status ID and belong to the specified user. These messages may also arrive before
  * a Tweet which falls into the specified range, although this is rare.
  * For more information see
  * <a href="https://dev.twitter.com/streaming/overview/messages-types#Location_deletion_notices_scrub_geo" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#Location_deletion_notices_scrub_geo</a>.
  */
final case class LocationDeletionNotice(scrub_geo: LocationDeletionId) extends CommonStreamingMessage

final case class LocationDeletionId(user_id: Long,
                              user_id_str: String,
                              up_to_status_id: Long,
                              up_to_status_id_str: String)

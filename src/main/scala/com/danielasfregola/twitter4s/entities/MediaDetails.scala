package com.danielasfregola.twitter4s.entities

case class MediaDetails(media_id: Long,
                        media_id_string: String,
                        expires_after_secs: Int,
                        size: Option[Long] = None,
                        image: Option[Image] = None,
                        video: Option[Video] = None,
                        processing_info: Option[ProcessingInfo] = None)

case class ProcessingInfo(state: String,
                          progress_percent: Long,
                          check_after_secs: Option[Int],
                          error: ProcessingError)

case class ProcessingError(code: Int, name: String, message: String)

package twitter4s.entities

case class MediaDetails(media_id: Long,
                        media_id_str: String,
                        expires_after_secs: Int,
                        size: Long,
                        image: Option[Image] = None,
                        video: Option[Video] = None)

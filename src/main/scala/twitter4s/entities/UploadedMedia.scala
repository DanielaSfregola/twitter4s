package twitter4s.entities

case class UploadedMedia(media_id: Long,
                         media_id_str: String,
                         size: Int,
                         image: Option[Image],
                         video: Option[Video])

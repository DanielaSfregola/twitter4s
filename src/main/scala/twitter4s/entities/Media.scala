package twitter4s.entities

import spray.http.Uri

case class Media(
                display_url: Uri,
                expanded_url: Uri,
                id: Long,
                id_str: String,
                indices: Seq[Int],
                media_url: Uri,
                media_url_https: Uri,
                size: Map[String, Size],
                source_status_id: Long,
                source_status_id_str: String,
                `type`: String,
                url: Uri
                )

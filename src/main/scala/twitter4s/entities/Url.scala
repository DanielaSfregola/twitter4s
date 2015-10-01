package twitter4s.entities

import spray.http.Uri

case class Url(
              indices: Seq[Int],
              url: Uri,
              display_url: String,
              expanded_url: Uri
              )

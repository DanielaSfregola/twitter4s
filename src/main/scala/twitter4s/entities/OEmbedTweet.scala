package twitter4s.entities

import spray.http.Uri

case class OEmbedTweet(author_name: Option[String],
                       author_url: Option[Uri],
                       cache_age: Option[String],
                       height: Option[Int],
                       html: String,
                       provider_url: Option[Uri],
                       provider_name: Option[String],
                       title: Option[String],
                       `type`: String,
                       url: Uri,
                       version: String,
                       width: Option[Int])


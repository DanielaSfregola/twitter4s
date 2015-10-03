package twitter4s.entities

import spray.http.Uri

case class LookupEntities(url: LookupUrls, description: LookupUrls)

case class LookupUrls(urls: Seq[LookupUrl])

case class LookupUrl( url: Uri, expanded_url: Uri, display_url: String, indices: Seq[Int])


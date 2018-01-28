package com.danielasfregola.twitter4s.entities

final case class LookupEntities(url: LookupUrls, description: LookupUrls)

final case class LookupUrls(urls: Seq[LookupUrl])

final case class LookupUrl(url: String, expanded_url: String, display_url: String, indices: Seq[Int])

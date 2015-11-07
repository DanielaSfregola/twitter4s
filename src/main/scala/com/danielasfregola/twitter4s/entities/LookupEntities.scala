package com.danielasfregola.twitter4s.entities

case class LookupEntities(url: LookupUrls, description: LookupUrls)

case class LookupUrls(urls: Seq[LookupUrl])

case class LookupUrl( url: String, expanded_url: String, display_url: String, indices: Seq[Int])


package com.danielasfregola.twitter4s.entities

final case class Webhook(id: String, url: String, valid: Boolean, created_timestamp: String) // TODO - convert to Instant? parsers doesn't support actual format

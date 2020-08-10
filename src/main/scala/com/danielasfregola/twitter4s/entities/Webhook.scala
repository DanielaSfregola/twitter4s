package com.danielasfregola.twitter4s.entities

import java.time.ZonedDateTime

final case class Webhook(id: String, url: String, valid: Boolean, created_timestamp: ZonedDateTime)

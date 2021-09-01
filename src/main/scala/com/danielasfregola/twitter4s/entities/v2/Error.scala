package com.danielasfregola.twitter4s.entities.v2

final case class Error(detail: String,
                       field: Option[String],
                       parameter: Option[String],
                       resource_id: Option[String],
                       resource_type: Option[String],
                       section: Option[String],
                       title: String,
                       `type`: Option[String],
                       value: Option[String])

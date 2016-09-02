package com.danielasfregola.twitter4s.http.clients.rest.media.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class MediaInitParameters(total_bytes: Long,
                               media_type: String,
                               additional_owners: Option[String],
                               command: String = "INIT") extends Parameters

package com.danielasfregola.twitter4s.http.clients.rest.media.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class MediaInitParameters(total_bytes: Long,
                                                        media_type: String,
                                                        media_category: Option[String],
                                                        additional_owners: Option[String],
                                                        command: String = "INIT")
    extends Parameters

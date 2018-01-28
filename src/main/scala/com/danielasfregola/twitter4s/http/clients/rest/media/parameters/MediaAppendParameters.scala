package com.danielasfregola.twitter4s.http.clients.rest.media.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class MediaAppendParameters(media_id: Long,
                                                          segment_index: Int,
                                                          media: String,
                                                          command: String = "APPEND")
    extends Parameters

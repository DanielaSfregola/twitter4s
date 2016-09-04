package com.danielasfregola.twitter4s.http.clients.rest.media.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] case class MediaFinalizeParameters(media_id: Long, command: String = "FINALIZE") extends Parameters

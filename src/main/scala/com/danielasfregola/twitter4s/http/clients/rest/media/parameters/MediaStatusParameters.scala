package com.danielasfregola.twitter4s.http.clients.rest.media.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class MediaStatusParameters(media_id: Long, command: String = "STATUS") extends Parameters

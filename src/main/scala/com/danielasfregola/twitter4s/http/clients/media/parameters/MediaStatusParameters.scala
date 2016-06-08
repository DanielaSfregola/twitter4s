package com.danielasfregola.twitter4s.http.clients.media.parameters

import com.danielasfregola.twitter4s.http.marshalling.Parameters

case class MediaStatusParameters(media_id: Long, command: String = "STATUS") extends Parameters

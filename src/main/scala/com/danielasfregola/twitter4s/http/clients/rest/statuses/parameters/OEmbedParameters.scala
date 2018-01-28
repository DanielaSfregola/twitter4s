package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.entities.enums.Alignment.Alignment
import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.enums.WidgetType.WidgetType
import com.danielasfregola.twitter4s.http.marshalling.Parameters

private[twitter4s] final case class OEmbedParameters(id: Option[Long],
                                                     url: Option[String],
                                                     maxwidth: Option[Int],
                                                     hide_media: Boolean,
                                                     hide_thread: Boolean,
                                                     omit_script: Boolean,
                                                     align: Alignment,
                                                     related: Seq[String],
                                                     lang: Language,
                                                     widget_type: Option[WidgetType],
                                                     hide_tweet: Boolean = false)
    extends Parameters

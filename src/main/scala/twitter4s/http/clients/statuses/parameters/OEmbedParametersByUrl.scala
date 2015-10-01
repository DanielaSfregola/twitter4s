package twitter4s.http.clients.statuses.parameters

import twitter4s.entities.enums.Alignment._
import twitter4s.entities.enums.Language._
import twitter4s.entities.enums.WidgetType._
import twitter4s.http.marshalling.Parameters

case class OEmbedParametersByUrl(url: String,
                                   maxwidth: Option[Int],
                                   hide_media: Boolean,
                                   hide_thread: Boolean,
                                   omit_script: Boolean,
                                   align: Alignment,
                                   related: Seq[String],
                                   lang: Language,
                                   widget_type: Option[WidgetType],
                                   hide_tweet: Boolean = false) extends Parameters

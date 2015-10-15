package twitter4s.http.clients.statuses.parameters

import twitter4s.entities.enums.Alignment.Alignment
import twitter4s.entities.enums.Language.Language
import twitter4s.entities.enums.WidgetType.WidgetType
import twitter4s.http.marshalling.Parameters

case class OEmbedParameters(id: Long,
                            maxwidth: Option[Int],
                            hide_media: Boolean,
                            hide_thread: Boolean,
                            omit_script: Boolean,
                            align: Alignment,
                            related: Seq[String],
                            lang: Language,
                            widget_type: Option[WidgetType],
                            hide_tweet: Boolean = false) extends Parameters

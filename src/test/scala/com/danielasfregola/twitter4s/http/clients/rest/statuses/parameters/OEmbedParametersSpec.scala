package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.entities.enums.{Alignment, Language, WidgetType}
import org.specs2.mutable.SpecificationLike

class OEmbedParametersSpec extends SpecificationLike {

  "OEmbedParameters" should {
    "correctly represents each field as the respective request parameter" in {
      OEmbedParameters(
        id = Some(1L),
        url = Some("https://example.com/"),
        maxwidth = Some(30),
        hide_media = true,
        hide_thread = true,
        omit_script = true,
        align = Alignment.Left,
        related = Vector("a", "b"),
        lang = Language.Japanese,
        widget_type = Some(WidgetType.Video),
        hide_tweet = true
      ).toString shouldEqual """align=left&
                               |hide_media=true&
                               |hide_thread=true&
                               |hide_tweet=true&
                               |id=1&
                               |lang=ja&
                               |maxwidth=30&
                               |omit_script=true&
                               |related=a%2Cb&
                               |url=https%3A%2F%2Fexample.com%2F&
                               |widget_type=video""".stripMargin.replace("\n", "")

    }
    "doesn't provide request parameter if the respective field is empty (tweet_mode is classic)" in {
      OEmbedParameters(
        id = None,
        url = None,
        maxwidth = None,
        hide_media = false,
        hide_thread = false,
        omit_script = false,
        align = Alignment.Left,
        related = Nil,
        lang = Language.Japanese,
        widget_type = None,
        hide_tweet = false
      ).toString shouldEqual """align=left&
                               |hide_media=false&
                               |hide_thread=false&
                               |hide_tweet=false&
                               |lang=ja&
                               |omit_script=false""".stripMargin.replace("\n", "")
    }
  }

}

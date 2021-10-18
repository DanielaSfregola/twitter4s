package com.danielasfregola.twitter4s

import java.net.URLEncoder

import scala.util.matching.Regex

package object http {

  implicit class RichString(val value: String) extends AnyVal {

    def urlEncoded = {
      val urlEncodePattern = """\+|\*|%7E""".r
      val replacer: Regex.Match => String = m =>
        m.group(0) match {
          case "+"   => "%20"
          case "*"   => "%2A"
          case "%7E" => "~"
      }
      val encodedValue = URLEncoder.encode(value, "UTF-8")
      urlEncodePattern.replaceAllIn(encodedValue, replacer)
    }
  }

}

package com.danielasfregola.twitter4s

import java.net.URLEncoder

import scala.util.matching.Regex

package object http {

  implicit class RichString(val value: String) extends AnyVal {

    def urlEncoded = {
      val urlEncodePattern = """\+|\*|%7E|%28|%29""".r
      val replacer: Regex.Match => String = m =>
        m.group(0) match {
          case "+"   => "%20"
          case "*"   => "%2A"
          case "%7E" => "~"
          case "%28" => "("
          case "%29" => ")"
      }
      val encodedValue = URLEncoder.encode(value, "UTF-8")
      urlEncodePattern.replaceAllIn(encodedValue, replacer)
    }
  }

}

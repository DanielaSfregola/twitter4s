package com.danielasfregola.twitter4s

import java.net.URLEncoder

import akka.http.scaladsl.model.Uri

package object http {

  implicit class RichString(val value: String) {

    def toAscii = urlEncoded.replace("+", "%20")

    def urlEncoded = URLEncoder.encode(value, "UTF-8")
  }

  implicit class RichUri(val uri: Uri) {

    def endpoint = s"${uri.scheme}:${uri.authority}${uri.path}"
  }

  // TODO - remove me!
  implicit class RichOldUri(val uri: spray.http.Uri) {

    def endpoint = s"${uri.scheme}:${uri.authority}${uri.path}"
  }
}

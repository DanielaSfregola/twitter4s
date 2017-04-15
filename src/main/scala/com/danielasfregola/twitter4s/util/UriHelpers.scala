package com.danielasfregola.twitter4s.util

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Query

trait UriHelpers {

  implicit def toRichUri(uri: Uri): RichUri = new RichUri(uri)

  class RichUri(val uri: Uri) {

    val endpoint: String = uri.withQuery(Query()).toString
  }

}

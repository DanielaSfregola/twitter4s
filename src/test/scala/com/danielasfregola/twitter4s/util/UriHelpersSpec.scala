package com.danielasfregola.twitter4s.util

import akka.http.scaladsl.model.Uri
import org.specs2.mutable.Specification

class UriHelpersSpec extends Specification with UriHelpers {

  "UriHelpers" should {

    "extract an endpoint representation" in {

      "from a uri" in {
        val uri = Uri("https://api.twitter.com/1.1/lists/members/create.json?param1=8044403&param2=daniela")
        uri.endpoint === "https://api.twitter.com/1.1/lists/members/create.json"
      }

      "from a uri with explicit port" in {
        val uri = Uri("http://example.com:8080/path?p=test")
        uri.endpoint === "http://example.com:8080/path"
      }
    }
  }
}

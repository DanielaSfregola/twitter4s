package com.danielasfregola.twitter4s.entities

import org.specs2.mutable.Specification

class ProfileImageSpec extends Specification {

  "ProfileImage" should {

    "generate 4 type of image sizes from the original picture url string" in {
      val profile = ProfileImage("test_size.ext")

      profile.default === "test.ext"
      profile.mini === "test_mini.ext"
      profile.normal === "test_normal.ext"
      profile.bigger === "test_bigger.ext"
    }
  }

}

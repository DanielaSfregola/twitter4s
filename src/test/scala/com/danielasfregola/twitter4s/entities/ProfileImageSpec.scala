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

    "return all image types as empty if original picture url string is blank" in {
      val emptyProfileImage = ProfileImage("")

      emptyProfileImage.default === ""
      emptyProfileImage.mini === ""
      emptyProfileImage.normal === ""
      emptyProfileImage.bigger === ""
    }

    "return all image types as empty if original picture ulr string is null" in {
      val emptyProfileImage = ProfileImage(null)

      emptyProfileImage.default === ""
      emptyProfileImage.mini === ""
      emptyProfileImage.normal === ""
      emptyProfileImage.bigger === ""
    }

  }

}

package com.danielasfregola.twitter4s.entities

import com.danielasfregola.twitter4s.helpers.FixturesSupport
import org.json4s.native.Serialization
import org.specs2.mutable.Specification

class ProfileImageSpec extends Specification with FixturesSupport {

  "ProfileImage" should {

    "generate 4 type of image sizes from the original picture url string" in {
      val profile = ProfileImage("test_size.ext")

      profile.default === "test.ext"
      profile.mini === "test_mini.ext"
      profile.normal === "test_normal.ext"
      profile.bigger === "test_bigger.ext"
    }

    "deserialized as None if empty profile image url is provided" in {
      val originalJson = load("/twitter/rest/users/empty-user-profile-url.json")

      val user = Serialization.read[User](originalJson)

      user.profile_image_url_https must beEmpty
      user.profile_image_url_https must beEmpty
    }
  }

}

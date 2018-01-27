package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.entities.enums.TweetMode
import org.specs2.mutable.SpecificationLike

class PostParametersSpec extends SpecificationLike {

  "PostParameters" should {
    "correctly represents each field as the respective request parameter" in {
      PostParameters(
        trim_user = true, tweet_mode = Some(TweetMode.Extended)
      ).toString shouldEqual "trim_user=true&tweet_mode=extended"
    }
    "doesn't provide request parameter if the respective optional field is empty" in {
      PostParameters(
        trim_user = false, tweet_mode = None
      ).toString shouldEqual "trim_user=false"
    }
  }

}

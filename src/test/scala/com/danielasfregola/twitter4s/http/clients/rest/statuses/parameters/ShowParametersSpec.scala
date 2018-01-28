package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.entities.enums.TweetMode
import org.specs2.mutable.SpecificationLike

class ShowParametersSpec extends SpecificationLike {

  "ShowParameters" should {
    "correctly represents each field as the respective request parameter" in {
      ShowParameters(
        id = 10L,
        trim_user = true,
        include_my_retweet = true,
        include_entities = true,
        tweet_mode = TweetMode.Extended
      ).toString shouldEqual
        """id=10&
          |include_entities=true&
          |include_my_retweet=true&
          |trim_user=true&
          |tweet_mode=extended""".stripMargin.replace("\n", "")
    }
    "doesn't provide request parameter if the respective field is empty (tweet_mode is classic)" in {
      ShowParameters(
        id = 10L,
        trim_user = false,
        include_my_retweet = false,
        include_entities = false,
        tweet_mode = TweetMode.Classic
      ).toString shouldEqual
        """id=10&
          |include_entities=false&
          |include_my_retweet=false&
          |trim_user=false""".stripMargin.replace("\n", "")
    }
  }

}

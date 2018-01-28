package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import com.danielasfregola.twitter4s.entities.enums.TweetMode
import org.specs2.mutable.SpecificationLike

class UserTimelineParametersSpec extends SpecificationLike {

  "UserTimelineParameters" should {
    "correctly represents each field as the respective request parameter" in {
      UserTimelineParameters(
        user_id = Some(10L),
        screen_name = Some("NaMe"),
        count = 20,
        since_id = Some(100L),
        max_id = Some(200L),
        trim_user = true,
        exclude_replies = true,
        contributor_details = true,
        include_rts = true,
        tweet_mode = TweetMode.Extended
      ).toString shouldEqual
        """contributor_details=true&
          |count=20&
          |exclude_replies=true&
          |include_rts=true&
          |max_id=200&
          |screen_name=NaMe&
          |since_id=100&
          |trim_user=true&
          |tweet_mode=extended&
          |user_id=10""".stripMargin.replace("\n", "")
    }
    "doesn't provide request parameter if the respective field is empty (tweet_mode is classic)" in {
      UserTimelineParameters(
        user_id = None,
        screen_name = None,
        count = 20,
        since_id = None,
        max_id = None,
        trim_user = false,
        exclude_replies = false,
        contributor_details = false,
        include_rts = false,
        tweet_mode = TweetMode.Classic
      ).toString shouldEqual
        """contributor_details=false&
          |count=20&
          |exclude_replies=false&
          |include_rts=false&
          |trim_user=false""".stripMargin.replace("\n", "")
    }
  }

}

package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import org.specs2.mutable.SpecificationLike

class LookupParametersSpec extends SpecificationLike {

  "LookupParameters" should {
    "correctly represents each field as the respective request parameter" in {
      LookupParameters(
        id = "some_id",
        include_entities = true,
        trim_user = true,
        map = true
      ).toString shouldEqual
        """id=some_id&
          |include_entities=true&
          |map=true&
          |trim_user=true""".stripMargin.replace("\n", "")
    }
    "doesn't provide request parameter if the respective field is empty (tweet_mode is classic)" in {
      LookupParameters(
        id = "some_id",
        include_entities = false,
        trim_user = false,
        map = false
      ).toString shouldEqual
        """id=some_id&
          |include_entities=false&
          |map=false&
          |trim_user=false""".stripMargin.replace("\n", "")
    }
  }

}

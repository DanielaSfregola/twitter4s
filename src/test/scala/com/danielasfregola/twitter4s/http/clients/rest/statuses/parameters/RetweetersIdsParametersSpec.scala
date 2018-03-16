package com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters

import org.specs2.mutable.SpecificationLike

class RetweetersIdsParametersSpec extends SpecificationLike {

  "RetweetersIdsParameters" should {
    "correctly represents each field as the respective request parameter" in {
      RetweetersIdsParameters(
        id = 20L,
        count = 20,
        cursor = 20L,
        stringify_ids = true
      ).toString shouldEqual "count=20&cursor=20&id=20&stringify_ids=true"
    }
  }

}

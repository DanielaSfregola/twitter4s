package com.danielasfregola.twitter4s.http.marshalling

import org.specs2.mutable.Specification

class BodyEncoderSpec extends Specification with BodyEncoder {

  "BodyEncoder" should {

    "encode a case class to a body with encoded params" in {
      case class TestData(e: Seq[(Double, Double)], d: Seq[Long], c: String, b: Option[Boolean], a: Int)

      val test =
        TestData(Seq((1, 2), (3, 4)), Seq(1, 2, 3), "Hello Ladies + Gentlemen, a signed OAuth request!", None, 5)
      val result = toBodyAsEncodedParams(test)
      result === "a=5&c=Hello%20Ladies%20%2B%20Gentlemen%2C%20a%20signed%20OAuth%20request%21&d=1%2C2%2C3&e=1.0%2C2.0%2C3.0%2C4.0"
    }

    "encode a case class to a body with params" in {
      case class TestData(e: Seq[(Double, Double)], d: Seq[Long], c: String, b: Option[Boolean], a: Int)

      val test = TestData(Seq((1, 2)), Seq(1, 2, 3), "Hello Ladies + Gentlemen, a signed OAuth request!", None, 5)
      val result = toBodyAsParams(test)
      result === "a=5&c=Hello Ladies + Gentlemen, a signed OAuth request!&d=1,2,3&e=1.0,2.0"
    }

  }

}

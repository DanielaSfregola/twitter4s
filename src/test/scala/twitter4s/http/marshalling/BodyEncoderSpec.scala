package twitter4s.http.marshalling

import org.specs2.mutable.Specification

class BodyEncoderSpec extends Specification with BodyEncoder {

  "BodyEncoder" should {

    "encode a case class to a body with params" in {
      case class TestData(c: String, b: Option[Boolean], a: Int)

      val test = TestData("nice string", None, 5)
      val result = toBodyAsParams(test)
      result === "a=5&c=nice%20string"
    }

  }

}


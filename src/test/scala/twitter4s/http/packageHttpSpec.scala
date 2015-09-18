package twitter4s.http

import org.specs2.mutable.Specification

class packageHttpSpec extends Specification {

  "Rich String" should {

    "convert a text to ascii encoding" in {
      "Ladies + Gentlemen".toAscii === "Ladies%20%2B%20Gentlemen"
      "An encoded string!".toAscii === "An%20encoded%20string%21"
      "Dogs, Cats & Mice".toAscii === "Dogs%2C%20Cats%20%26%20Mice"
      "☃".toAscii === "%E2%98%83"
    }
  }
}

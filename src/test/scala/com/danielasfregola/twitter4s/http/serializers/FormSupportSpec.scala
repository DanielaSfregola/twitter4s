package com.danielasfregola.twitter4s.http.serializers

import org.specs2.mutable.Specification
import org.specs2.specification.Scope


class FormSupportSpec extends Specification {

  abstract class FormSupportContext extends FormSupport with Scope

  "FormSupport" should {

    "map a form string into a case class" in new FormSupportContext {
      case class MyTest(a: String, b: String, c: String)

      val text = "a=hello&c=test&b=foobar"
      to[MyTest].from(text) === Some(MyTest(a = "hello", b = "foobar", c = "test"))
    }
  }
}

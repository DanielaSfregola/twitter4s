package com.danielasfregola.twitter4s.http.serializers

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import com.danielasfregola.twitter4s.exceptions.TwitterException
import com.danielasfregola.twitter4s.helpers.{AwaitableFuture, TestActorSystem, TestExecutionContext}
import org.specs2.mutable.SpecificationLike
import org.specs2.specification.Scope

import scala.concurrent.Future

class FormSupportSpec extends TestActorSystem with SpecificationLike with AwaitableFuture with TestExecutionContext {

  case class MyTest(a: String, b: String, c: String, d: Boolean)

  implicit object MyTestFromMap extends FromMap[MyTest] {
    def apply(m: Map[String, String]) =
      for {
        a <- m.get("a")
        b <- m.get("b")
        c <- m.get("c")
        d <- m.get("d")
      } yield MyTest(a, b, c, toBoolean(d))
  }

  abstract class FormSupportContext extends Scope

  def buildResponse(text: String): HttpResponse =
    HttpResponse(entity = HttpEntity.apply(ContentTypes.`text/html(UTF-8)`, text))

  "FormSupport" should {

    "unmarshall a well-formed text into a case class" in new FormSupportContext {
      val text = "a=hello&c=test&b=foobar&d=true"
      val result: Future[MyTest] = FormSupport.unmarshallText[MyTest](buildResponse(text))
      result.await === MyTest(a = "hello", b = "foobar", c = "test", d = true)
    }

    "throw TwitterException if text is not well formed" in new FormSupportContext {
      val text = "non-well-formed-string"
      val result: Future[MyTest] = FormSupport.unmarshallText[MyTest](buildResponse(text))
      result.await should throwA[TwitterException]
    }
  }
}

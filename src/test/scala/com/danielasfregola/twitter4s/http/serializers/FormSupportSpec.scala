package com.danielasfregola.twitter4s.http.serializers

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import com.danielasfregola.twitter4s.exceptions.TwitterException
import org.specs2.specification.Scope
import com.danielasfregola.twitter4s.helpers.{AwaitableFuture, TestActorSystem, TestExecutionContext}
import org.specs2.mutable.SpecificationLike
import shapeless.LabelledGeneric

import scala.concurrent.Future


class FormSupportSpec
  extends TestActorSystem
  with SpecificationLike
  with AwaitableFuture
  with TestExecutionContext {

  abstract class FormSupportContext extends Scope {
    implicit val lgen = LabelledGeneric[MyTest]
  }

  case class MyTest(a: String, b: String, c: String, d: Boolean)

  def buildResponse(text: String): HttpResponse =
    HttpResponse(entity = HttpEntity.apply(ContentTypes.`text/html(UTF-8)`, text))

  "FormSupport" should {

    "unmarshall a well-formed text into a case class" in new FormSupportContext {
      val text = "a=hello&c=test&b=foobar&d=true"
      val result: Future[MyTest] = FormSupport.unmarshallText[MyTest, lgen.Repr](buildResponse(text))
       result.await === MyTest(a = "hello", b = "foobar", c = "test", d = true)
    }

    "throw TwitterException if text is not well formed" in new FormSupportContext {
      val text = "non-well-formed-string"
      val result: Future[MyTest] = FormSupport.unmarshallText[MyTest, lgen.Repr](buildResponse(text))
      result.await should throwA[TwitterException]
    }
  }
}

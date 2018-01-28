package com.danielasfregola.twitter4s.entities

import com.danielasfregola.twitter4s.helpers.{FixturesSupport, JsonDiffSupport}
import org.json4s.native.Serialization.writePretty
import org.json4s.native.{JsonParser, Serialization}
import org.json4s.{JNothing, JValue}
import org.specs2.matcher.{Expectable, Matcher}
import org.specs2.mutable.Specification
import org.specs2.specification.core.Fragment

import scala.reflect._

class DeserializationRoundtripSpec extends Specification with FixturesSupport with JsonDiffSupport {

  "JSON deserialization" should {

    def roundtripTest[T <: AnyRef: Manifest](jsonFile: String): Fragment = {

      val className = classTag[T].runtimeClass.getSimpleName

      s"round-trip successfully for $className in $jsonFile" in {
        val originalJson = load(jsonFile)

        val deserializedEntity = Serialization.read[T](originalJson)

        val serializedJson = Serialization.writePretty[T](deserializedEntity)

        originalJson must beASubsetOfJson(serializedJson)
      }
    }

    roundtripTest[User]("/twitter/rest/users/user.json")
  }

  def beASubsetOfJson(otherJson: String): Matcher[String] = new Matcher[String] {

    def apply[S <: String](t: Expectable[S]) = {
      val alpha: JValue = JsonParser.parse(t.value)
      val beta: JValue = JsonParser.parse(otherJson)

      jsonDiff(alpha, beta) match {
        case diff @ JsonDiff(JNothing, _, JNothing) =>
          success(s"""${t.value}
               |is a subset of
               |$otherJson
               |${renderDiff(diff)}
             """.stripMargin,
                  t)
        case diff =>
          failure(s"""${t.value}
               |is not a subset of
               |$otherJson
               |${renderDiff(diff)}
             """.stripMargin,
                  t)
      }
    }

    private def renderDiff(diff: JsonDiff) = {
      val changed = diff.changed.toOption.map { c =>
        s"""Changed:
           |${writePretty(c)}
          """.stripMargin
      }
      val deleted = diff.deleted.toOption.map { d =>
        s"""Deleted:
           |${writePretty(d)}
          """.stripMargin
      }
      val added = diff.added.toOption.map { a =>
        s"""Added:
           |${writePretty(a)}
          """.stripMargin
      }

      (changed ++ deleted ++ added).mkString
    }
  }
}

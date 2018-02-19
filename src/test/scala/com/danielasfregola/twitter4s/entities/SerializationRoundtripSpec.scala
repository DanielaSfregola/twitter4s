package com.danielasfregola.twitter4s.entities

import java.time.Instant

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import com.danielasfregola.twitter4s.http.serializers.JsonSupport
import org.json4s.native.Serialization
import org.scalacheck.Gen.alphaChar
import org.scalacheck.{Arbitrary, Gen}
import org.specs2.mutable.Specification
import org.specs2.specification.core.Fragment

import scala.reflect._

class SerializationRoundtripSpec extends Specification with RandomDataGenerator with JsonSupport {

  "JSON serialization" should {

    def roundtripTest[T <: AnyRef: Manifest: Arbitrary]: Fragment = {

      val className = classTag[T].runtimeClass.getSimpleName

      s"round-trip successfully for $className" in {
        val randomEntity = random[T]

        val serializedJson = Serialization.write[T](randomEntity)

        val deserializedEntity = Serialization.read[T](serializedJson)

        deserializedEntity === randomEntity
      }
    }

    roundtripTest[User]
  }

  // We serialize dates to second precision
  implicit val arbitraryDate: Arbitrary[Instant] = Arbitrary {
    for {
      timeInSeconds: Long <- Gen.chooseNum(1142899200L, 1512442349L)
    } yield Instant.ofEpochSecond(timeInSeconds)
  }

  implicit val arbitraryProfileImage: Arbitrary[ProfileImage] = Arbitrary {
    for {
      prefix: String <- Gen.nonEmptyListOf(alphaChar).map(_.mkString)
      suffix: String <- Gen.oneOf("_mini", "_normal", "_bigger", "")
    } yield ProfileImage(s"${prefix}_$suffix.jpg")
  }
}

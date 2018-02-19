package com.danielasfregola.twitter4s.entities

import java.text.SimpleDateFormat
import java.util.Locale

import akka.http.scaladsl.model.headers.RawHeader
import com.danielasfregola.twitter4s.exceptions.TwitterException
import org.specs2.mutable.Specification

class RateLimitSpec extends Specification {

  "RateLimit" should {

    "be created from http headers" in {
      val headers = Seq(RawHeader("x-rate-limit-limit", "15"),
                        RawHeader("x-rate-limit-remaining", "14"),
                        RawHeader("x-rate-limit-reset", "1445181993"))

      val rateLimit = RateLimit(headers)

      val expectedInstant = {
        val dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH)
        dateFormatter.parse("Sun Oct 18 15:26:33 +0000 2015").toInstant
      }

      rateLimit.limit === 15
      rateLimit.remaining === 14
      rateLimit.reset === expectedInstant
    }

    "throw exception if no rate http headers found" in {
      RateLimit(Seq.empty) should throwA[TwitterException]
    }

  }

}

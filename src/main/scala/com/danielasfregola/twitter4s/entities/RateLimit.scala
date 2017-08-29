package com.danielasfregola.twitter4s.entities

import java.util.Date

import akka.http.scaladsl.model.{HttpHeader, StatusCodes}
import com.danielasfregola.twitter4s.exceptions.TwitterException

final case class RateLimit(limit: Int, remaining: Int, reset: Date)

object RateLimit {

  def apply(limit: Int, remaining: Int, reset: Long): RateLimit = {
    val epochInMillis = reset * 1000
    apply(limit, remaining, new Date(epochInMillis))
  }

  def apply(headers: Seq[HttpHeader]): RateLimit = {
    val errorMsg =
      s"""Rate Information expected but not found.
         |
         |Please report it at https://github.com/DanielaSfregola/twitter4s/issues/new
         |Headers names were: ${headers.map(_.lowercaseName).mkString(", ")}""".stripMargin

    def extractHeaderValue[T](name: String)(f: String => T): T =
      headers
        .find(_.lowercaseName == name)
        .map(h => f(h.value))
        .getOrElse(throw TwitterException(StatusCodes.InternalServerError, errorMsg))

    val limit = extractHeaderValue("x-rate-limit-limit")(_.toInt)
    val remaining = extractHeaderValue("x-rate-limit-remaining")(_.toInt)
    val reset = extractHeaderValue("x-rate-limit-reset")(_.toLong)
    apply(limit, remaining, reset)
  }

}

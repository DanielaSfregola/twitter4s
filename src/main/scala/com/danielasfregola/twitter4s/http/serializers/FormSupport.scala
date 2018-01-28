package com.danielasfregola.twitter4s.http.serializers

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.stream.Materializer
import com.danielasfregola.twitter4s.exceptions.TwitterException

import scala.concurrent.Future
import scala.concurrent.duration._

private[twitter4s] trait FromMap[T] {
  def apply(m: Map[String, String]): Option[T]

  def toBoolean(text: String): Boolean = text.trim.toLowerCase == "true"
}

private[twitter4s] object FormSupport {

  def unmarshallText[T: Manifest](response: HttpResponse)(implicit materializer: Materializer,
                                                          fromMap: FromMap[T]): Future[T] = {
    implicit val ec = materializer.executionContext
    response.entity.toStrict(5 seconds).map(_.data.decodeString("UTF-8")).map { body =>
      fromMap(asMap(body)).getOrElse(throw unmarshallError[T](body))
    }
  }

  private def asMap(body: String): Map[String, String] =
    body
      .split("&")
      .map { fields =>
        val tokens = fields.split("=", 2)
        tokens.head -> tokens.tail.mkString.trim
      }
      .toMap

  private def unmarshallError[T: Manifest](body: String): TwitterException = {
    val errorMsg =
      s"""Could not serialise body to ${manifest[T].runtimeClass.getSimpleName}.
         |
         |Please report it at https://github.com/DanielaSfregola/twitter4s/issues/new
         |Body was: $body""".stripMargin
    TwitterException(StatusCodes.InternalServerError, errorMsg)
  }
}

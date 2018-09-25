package com.danielasfregola.twitter4s.http.serializers

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.{Date, Locale}

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.JsonAST.{JDouble, JInt, JLong, JString}
import org.json4s.{CustomSerializer, DefaultFormats, Formats, Serialization, native}

import scala.util.Try

private[twitter4s] trait JsonSupport extends Json4sSupport {

  val serialization: Serialization = native.Serialization

  private implicit class FormatsLifter(f: Formats) {
    def +(fc: FormatsComposer): Formats =
      fc.compose(f)
  }

  implicit lazy val json4sFormats: Formats =
    defaultFormats + StreamingMessageFormats + CustomFormats + EnumFormats + new StrToLongFormatter

  private val defaultFormats = new DefaultFormats {
    override def dateFormatter = DateTimeFormatter.formatter
  }.preservingEmptyValues

}

private[twitter4s] object DateTimeFormatter {

  val formatterPattern = "EEE MMM dd HH:mm:ss ZZZZ yyyy"
  val locale = Locale.ENGLISH

  // SimpleDateFormat not thread safe! Damn you java!!!
  def formatter = new SimpleDateFormat(formatterPattern, locale)

  def canParseInstant(s: String): Boolean = Try(parseInstant(s)).isSuccess

  def parseInstant(s: String): Instant = formatter.parse(s).toInstant

  def formatInstant(i: Instant): String = formatter.format(Date.from(i))

}

private[twitter4s] class StrToLongFormatter
    extends CustomSerializer[Long](
      formats =>
        (
          {
            case JInt(x)    => x.toLong
            case JDouble(x) => x.toLong
            case JLong(x)   => x.toLong
            case JString(x) => x.toLong
          }, { case x: Long => JLong(x) }
      ))

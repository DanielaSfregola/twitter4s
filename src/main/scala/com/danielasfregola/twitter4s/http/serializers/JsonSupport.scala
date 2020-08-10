package com.danielasfregola.twitter4s.http.serializers

import java.text.SimpleDateFormat
import java.time.{Instant, ZonedDateTime}
import java.time.format.DateTimeFormatterBuilder
import java.util.{Date, Locale}

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, Formats, Serialization, native}

import scala.util.Try

private[twitter4s] trait JsonSupport extends Json4sSupport {

  val serialization: Serialization = native.Serialization

  private implicit class FormatsLifter(f: Formats) {
    def +(fc: FormatsComposer): Formats =
      fc.compose(f)
  }

  implicit lazy val json4sFormats: Formats =
    defaultFormats + StreamingMessageFormats + CustomFormats + EnumFormats

  private val defaultFormats = new DefaultFormats {
    override def dateFormatter = DateTimeFormatter.formatter
  }.preservingEmptyValues

}

private[twitter4s] object DateTimeFormatter {

  val formatterPattern = "EEE MMM dd HH:mm:ss ZZZZ yyyy"
  val locale = Locale.ENGLISH

  // SimpleDateFormat not thread safe! Damn you java!!!
  def formatter = new SimpleDateFormat(formatterPattern, locale)

  val builder: DateTimeFormatterBuilder = new DateTimeFormatterBuilder()
    .appendPattern("[EEE MMM dd HH:mm:ss Z yyyy]")
    .appendPattern("[yyyy-MM-dd HH:mm:ss Z]")

  val dateTimeFormatter = builder.toFormatter(locale)

  def canParseInstant(s: String): Boolean = Try(parseInstant(s)).isSuccess

  def parseInstant(s: String): Instant = formatter.parse(s).toInstant

  def parseInstant(l: Long): Instant = Instant.ofEpochSecond(l)

  def formatInstant(i: Instant): String = formatter.format(Date.from(i))

  def parseZonedDateTime(s: String): ZonedDateTime = ZonedDateTime.parse(s, dateTimeFormatter)

}

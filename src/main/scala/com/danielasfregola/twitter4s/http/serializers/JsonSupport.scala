package com.danielasfregola.twitter4s.http.serializers

import java.text.SimpleDateFormat
import java.util.Locale

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, Formats, Serialization, native}

trait JsonSupport extends Json4sSupport {

  val serialization: Serialization = native.Serialization

  private implicit class FormatsLifter(f: Formats) {
    def +(fc: FormatsComposer): Formats =
      fc.compose(f)
  }

  implicit lazy val json4sFormats: Formats =
    defaultFormats + StreamingMessageFormats + CustomFormats + EnumFormats

  private val defaultFormats = new DefaultFormats {
    override def dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH)
  }.preservingEmptyValues

}

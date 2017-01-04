package com.danielasfregola.twitter4s.http.serializers

import java.text.SimpleDateFormat
import java.util.Locale

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, Formats, native}

trait JsonSupport extends Json4sSupport {

  implicit val serialization = native.Serialization

  implicit lazy val json4sFormats: Formats =
    defaultFormats ++ CustomSerializers.all ++ EnumSerializers.all ++ StreamingMessageSerializers.all

  private val defaultFormats = new DefaultFormats {
    override def dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH)
  }.preservingEmptyValues

}

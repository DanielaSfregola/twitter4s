package com.danielasfregola.twitter4s.http.serializers

import java.time._

import com.danielasfregola.twitter4s.entities.enums.DisconnectionCode
import com.danielasfregola.twitter4s.entities.enums.DisconnectionCode.DisconnectionCode
import com.danielasfregola.twitter4s.entities.ProfileImage
import com.danielasfregola.twitter4s.entities.v2.GeoJSON
import org.json4s.JsonAST.{JInt, JLong, JNull, JObject, JString}
import org.json4s.native.Printer.compact
import org.json4s.native.{JsonParser, renderJValue}
import org.json4s.{CustomSerializer, Formats, JArray, JDouble}

private[twitter4s] object CustomFormats extends FormatsComposer {

  override def compose(f: Formats): Formats = {
    f + InstantSerializer + LocalDateSerializer + DisconnectionCodeSerializer + ProfileImageSerializer + ZonedDateTimeSerializer + CoordinateSerializer + GeoJSONSerializer
  }

}

private[twitter4s] case object InstantSerializer
    extends CustomSerializer[Instant](format =>
      ({
        case JInt(i)                                            => DateTimeFormatter.parseInstant(i.toLong)
        case JLong(l)                                           => DateTimeFormatter.parseInstant(l)
        case JString(s) if DateTimeFormatter.canParseInstant(s) => DateTimeFormatter.parseInstant(s)
        case JString(stringAsUnixTime) if stringAsUnixTime.forall(_.isDigit) =>
          Instant.ofEpochMilli(stringAsUnixTime.toLong)
      }, {
        case instant: Instant => JString(DateTimeFormatter.formatInstant(instant))
      }))

private[twitter4s] case object LocalDateSerializer
    extends CustomSerializer[LocalDate](format =>
      ({
        case JString(dateString) =>
          dateString.split("-") match {
            case Array(year, month, date) => LocalDate.of(year.toInt, month.toInt, date.toInt)
            case _                        => null
          }
        case JNull => null
      }, {
        case date: LocalDate => JString(date.toString)
      }))

private[twitter4s] case object ZonedDateTimeSerializer
    extends CustomSerializer[ZonedDateTime](_ =>
      ({
        case JString(dateString) => DateTimeFormatter.parseZonedDateTime(dateString)
        case JNull               => null
      }, {
        case date: ZonedDateTime => JString(date.toString)
      }))

private[twitter4s] case object DisconnectionCodeSerializer
    extends CustomSerializer[DisconnectionCode](format =>
      ({
        case JInt(n) => DisconnectionCode(n.toInt)
        case JNull   => null
      }, {
        case code: DisconnectionCode => JInt(code.id)
      }))

private[twitter4s] case object ProfileImageSerializer
    extends CustomSerializer[ProfileImage](format =>
      ({
        case JString("") => null // withheld tweets provide urls as empty strings, making equivalent to null
        case JString(n)  => ProfileImage(n)
        case JNull       => null
      }, {
        case img: ProfileImage => JString(img.normal)
      }))

private[twitter4s] case object CoordinateSerializer
    extends CustomSerializer[(Double, Double)](format =>
      ({
        case JArray(List(JDouble(lat), JDouble(long))) => (lat, long)
      }, {
        case (lat: Double, long: Double) => JArray(List(JDouble(lat), JDouble(long)))
      }))

private[twitter4s] case object GeoJSONSerializer
    extends CustomSerializer[GeoJSON](format =>
      ({
        case x: JObject => GeoJSON(compact(renderJValue(x)))
      }, {
        case GeoJSON(s) => JsonParser.parse(s)
      }))

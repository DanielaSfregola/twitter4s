package com.danielasfregola.twitter4s.http.serializers

import java.time._

import com.danielasfregola.twitter4s.entities.enums.DisconnectionCode
import com.danielasfregola.twitter4s.entities.enums.DisconnectionCode.DisconnectionCode
import com.danielasfregola.twitter4s.entities.{DirectMessageId, ProfileImage}
import org.json4s.JsonAST.{JInt, JNull, JString}
import org.json4s.{CustomSerializer, Formats}

private[twitter4s] object CustomFormats extends FormatsComposer {

  override def compose(f: Formats): Formats =
    f + InstantSerializer + LocalDateSerializer + DisconnectionCodeSerializer + ProfileImageSerializer + DirectMessageIdFormatter

}

private[twitter4s] case object InstantSerializer
    extends CustomSerializer[Instant](format =>
      ({
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
        case JString(n) => ProfileImage(n)
        case JNull      => null
      }, {
        case img: ProfileImage => JString(img.normal)
      }))

private[twitter4s] case object DirectMessageIdFormatter
    extends CustomSerializer[DirectMessageId](format =>
      ({
        case JString(value)         => DirectMessageId(value.toLong)
      }, { case DirectMessageId(id) => JString(id.toString) }))

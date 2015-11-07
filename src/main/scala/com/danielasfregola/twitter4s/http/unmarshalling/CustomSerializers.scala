package com.danielasfregola.twitter4s.http.unmarshalling

import java.time.LocalDate

import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JNull, JString}

object CustomSerializers {

  val all = List(LocalDateSerializer)

}

case object LocalDateSerializer extends CustomSerializer[LocalDate](format =>
  ({
    case JString(dateString) =>
      dateString.split("-") match {
        case Array(year, month, date) => LocalDate.of(year.toInt, month.toInt, date.toInt)
        case _ => null
      }
    case JNull => null
  },
  {
    case date: LocalDate => JString(date.toString)
  }))

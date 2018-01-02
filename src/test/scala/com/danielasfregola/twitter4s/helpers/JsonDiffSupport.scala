package com.danielasfregola.twitter4s.helpers

import java.util.Date

import com.danielasfregola.twitter4s.http.serializers.JsonSupport
import org.json4s.JsonAST._
import org.json4s.{JField, JNothing, JObject}

/**
 * This is a copy of [[org.json4s.Diff]], with modifications to make it
 * treat equivalent dates in different timezones as equal, and empty arrays
 * as equal to nulls.
 */
trait JsonDiffSupport { this: JsonSupport =>

  /** A difference between two JSONs (j1 diff j2).
   *
   * @param changed what has changed from j1 to j2
   * @param added   what has been added to j2
   * @param deleted what has been deleted from j1
   */
  case class JsonDiff(changed: JValue, added: JValue, deleted: JValue) {

    private[helpers] def toField(name: String): JsonDiff = {
      def applyTo(x: JValue) = x match {
        case JNothing => JNothing
        case _ => JObject((name, x))
      }

      JsonDiff(applyTo(changed), applyTo(added), applyTo(deleted))
    }
  }

  def jsonDiff(val1: JValue, val2: JValue): JsonDiff = (val1, val2) match {
    case (x, y) if x == y => JsonDiff(JNothing, JNothing, JNothing)
    case (JObject(xs), JObject(ys)) => diffFields(xs, ys)
    // treat empty collections as equal to nulls
    case (JArray(Nil), JNothing | JNull) | (JNothing | JNull, JArray(Nil)) => JsonDiff(JNothing, JNothing, JNothing)
    case (JArray(xs), JArray(ys)) => diffVals(xs, ys)
    // unlike diff of JArrays, order of elements is ignored in diff of JSets
    case (JSet(x), JSet(y)) if JSet(x) != JSet(y) => JsonDiff(JNothing, JSet(y).difference(JSet(x)), JSet(x).difference(JSet(y)))
    case (JInt(x), JInt(y)) if x != y => JsonDiff(JInt(y), JNothing, JNothing)
    case (JDouble(x), JDouble(y)) if x != y => JsonDiff(JDouble(y), JNothing, JNothing)
    case (JDecimal(x), JDecimal(y)) if x != y => JsonDiff(JDecimal(y), JNothing, JNothing)
    // if two strings can be parsed as dates, check equality for the dates
    case (JDate(x), JDate(y)) if x == y => JsonDiff(JNothing, JNothing, JNothing)
    case (JString(x), JString(y)) if x != y => JsonDiff(JString(y), JNothing, JNothing)
    case (JBool(x), JBool(y)) if x != y => JsonDiff(JBool(y), JNothing, JNothing)
    case (JNothing, x) => JsonDiff(JNothing, x, JNothing)
    case (x, JNothing) => JsonDiff(JNothing, JNothing, x)
    case (x, y) => JsonDiff(y, JNothing, JNothing)
  }

  private def diffFields(vs1: List[JField], vs2: List[JField]) = {
    def diffRec(xleft: List[JField], yleft: List[JField]): JsonDiff = xleft match {
      case Nil => JsonDiff(JNothing, if (yleft.isEmpty) JNothing else JObject(yleft), JNothing)
      case x :: xs => yleft find (_._1 == x._1) match {
        case Some(y) =>
          val JsonDiff(c1, a1, d1) = jsonDiff(x._2, y._2).toField(y._1)
          val JsonDiff(c2, a2, d2) = diffRec(xs, yleft filterNot (_ == y))
          JsonDiff(c1 merge c2, a1 merge a2, d1 merge d2)
        case None =>
          val JsonDiff(c, a, d) = diffRec(xs, yleft)
          JsonDiff(c, a, JObject(x :: Nil) merge d)
      }
    }

    diffRec(vs1, vs2)
  }

  private def diffVals(vs1: List[JValue], vs2: List[JValue]) = {
    def diffRec(xleft: List[JValue], yleft: List[JValue]): JsonDiff = (xleft, yleft) match {
      case (xs, Nil) => JsonDiff(JNothing, JNothing, if (xs.isEmpty) JNothing else JArray(xs))
      case (Nil, ys) => JsonDiff(JNothing, if (ys.isEmpty) JNothing else JArray(ys), JNothing)
      case (x :: xs, y :: ys) =>
        val JsonDiff(c1, a1, d1) = jsonDiff(x, y)
        val JsonDiff(c2, a2, d2) = diffRec(xs, ys)
        JsonDiff(c1 ++ c2, a1 ++ a2, d1 ++ d2)
    }

    diffRec(vs1, vs2)
  }

  private object JDate {
    def unapply(js: JString): Option[Date] = json4sFormats.dateFormat.parse(js.s)
  }
}

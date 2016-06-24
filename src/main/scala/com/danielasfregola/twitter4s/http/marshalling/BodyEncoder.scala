package com.danielasfregola.twitter4s.http
package marshalling

trait BodyEncoder {

  def toBodyAsParams(cc: Product): String =
    toBodyAsMap(cc).map{ case (k, v) =>
      val key = k.replace("$colon", ":")
      s"$key=$v"
    }.toList.sorted.mkString("&")

  def toBodyAsEncodedParams(cc: Product): String =
    toBodyAsMap(cc).map{ case (k, v) =>
      val key = k.replace("$colon", ":")
      s"$key=${v.urlEncoded}"
    }.toList.sorted.mkString("&")

  private def toBodyAsMap(cc: Product): Map[String, String] =
    asMap(cc).flatMap {
      case (k, head :: tail) => Some(k -> (head +: tail).mkString(","))
      case (k, Nil) => None
      case (k, None) => None
      case (k, Some("")) => None
      case (k, Some(v)) => Some(k -> v.toString)
      case (k, v) => Some(k -> v.toString)
    }

  // TODO - improve performance with Macros?
  private def asMap(cc: Product): Map[String, Any] =
    cc.getClass.getDeclaredFields.map( _.getName ).zip( cc.productIterator.to ).toMap

}

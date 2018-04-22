package com.danielasfregola.twitter4s.http
package marshalling

trait BodyEncoder {

  def toBodyAsParams(cc: Product): String =
    toBodyAsMap(cc)
      .map {
        case (k, v) =>
          val key = k.replace("$colon", ":")
          s"$key=$v"
      }
      .toList
      .sorted
      .mkString("&")

  def toBodyAsEncodedParams(cc: Product): String =
    toBodyAsMap(cc)
      .map {
        case (k, v) =>
          val key = k.replace("$colon", ":")
          s"$key=${v.urlEncoded}"
      }
      .toList
      .sorted
      .mkString("&")

  private def toBodyAsMap(cc: Product): Map[String, String] =
    asMap(cc).flatMap {
      case (k, (v1, v2) :: tail) =>
        val rest = tail.map { case (a, b) => s"$a,$b" }
        val flattened = s"$v1,$v2" :: rest
        Some(k -> flattened.mkString(","))
      case (_, Seq())                         => None
      case (k, s @ Seq(_*))                   => Some(k -> s.mkString("", ",", ""))
      case (_, None)                          => None
      case (_, Some(v)) if v.toString.isEmpty => None
      case (k, Some(v))                       => Some(k -> v.toString)
      case (k, v) if v.toString.isEmpty       => None
      case (k, v)                             => Some(k -> v.toString)
    }

  // TODO - improve performance with Macros?
  private def asMap(cc: Product): Map[String, Any] =
    cc.getClass.getDeclaredFields.map(_.getName).zip(cc.productIterator.to).toMap

}

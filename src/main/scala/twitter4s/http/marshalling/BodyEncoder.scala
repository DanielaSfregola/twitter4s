package twitter4s.http
package marshalling

trait BodyEncoder {

  def toBodyAsParams(cc: Product): String = {
    val asMap = toMap(cc)
    asMap.map{
      case (k, None) => None
      case (k, Some(v)) => Some(s"$k=${v.toString.toAscii}")
      case (k, v) => Some(s"$k=${v.toString.toAscii}")
    }.flatten.toList.sorted.mkString("&")
  }

  // TODO - improve performance with Macros
  private def toMap(cc: Product): Map[String, Any] =
    cc.getClass.getDeclaredFields.map( _.getName ).zip( cc.productIterator.to ).toMap

}

package twitter4s.http
package marshalling

trait BodyEncoder {

  // TODO - we could improve this with macros...

  def toBodyAsParams(cc: Product): String = {
    val asMap = toMap(cc)
    asMap.map{
      case (k, None) => None
      case (k, Some(v)) => Some(s"$k=${v.toString.toAscii}")
      case (k, v) => Some(s"$k=${v.toString.toAscii}")
    }.flatten.toList.sorted.mkString("&")
  }

  def toMap(cc: Product): Map[String, Any] = {
    val values = cc.productIterator
    cc.getClass.getDeclaredFields.map( _.getName -> values.next ).toMap
  }
}

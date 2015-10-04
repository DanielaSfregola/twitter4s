package twitter4s.http
package marshalling

trait BodyEncoder {

  def toBodyAsParams(cc: Product): String =
    toBodyAsMap(cc).map{ case (k, v) => s"$k=$v"}.toList.sorted.mkString("&")

  def toBodyAsEncodedParams(cc: Product): String =
    toBodyAsMap(cc).map{ case (k, v) => s"$k=${v.urlEncoded}"}.toList.sorted.mkString("&")

  
  private def toBodyAsMap(cc: Product): Map[String, String] =
    asMap(cc).map {
      case (k, None) => None
      case (k, Some(v)) => Some(k -> v.toString)
      case (k, Seq()) => None
      case (k, v) => Some(k -> v.toString)
    }.flatten.toMap
    
  

  // TODO - improve performance with Macros
  private def asMap(cc: Product): Map[String, Any] =
    cc.getClass.getDeclaredFields.map( _.getName ).zip( cc.productIterator.to ).toMap

}

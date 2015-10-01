package twitter4s.http
package unmarshalling

import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JNull, JString}
import spray.http.Uri

object CustomSerializers {

  val all = List(CustomUriSerializer)
}

case object CustomUriSerializer extends CustomSerializer[Uri](format =>
  ({
    case JString(uri) => Uri(uri.urlDecoded)
    case JNull => null
  },
  {
    case uri: Uri => JString(uri.toString)
  }))

package twitter4s.http.unmarshalling

import java.lang.reflect.InvocationTargetException
import java.text.SimpleDateFormat

import org.json4s.native.Serialization
import org.json4s.{DefaultFormats, Formats, MappingException}
import spray.http.{HttpCharsets, HttpEntity, MediaTypes}
import spray.httpx.unmarshalling.Unmarshaller

trait JsonSupport {

  implicit def json4sFormats: Formats = new DefaultFormats {
    override def dateFormatter = {
      val simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy")
      simpleDateFormat
    }
  }

  implicit def json4sUnmarshaller[T: Manifest] = {
    Unmarshaller[T](MediaTypes.`application/json`) {
      case x: HttpEntity.NonEmpty ⇒
        try Serialization.read[T](x.asString(defaultCharset = HttpCharsets.`UTF-8`))
        catch {
          case MappingException("unknown error", ite: InvocationTargetException) ⇒ throw ite.getCause
        }
    }
  }
}

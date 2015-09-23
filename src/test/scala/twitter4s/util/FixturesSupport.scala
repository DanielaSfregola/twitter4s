package twitter4s.util

import scala.io.Source

import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import twitter4s.http.unmarshalling.JsonSupport

trait FixturesSupport extends JsonSupport {

  def loadJson(resourcePath: String) = Source.fromURL(getClass.getResource(resourcePath)).mkString
  def loadJsonAs[T: Manifest](resourcePath: String): T = readJsonAs[T](loadJson(resourcePath))

  def readJsonAs[T: Manifest](json: String): T = Serialization.read[T](json)
  def printAsJson[T <: AnyRef](value: T) = println(pretty(render(parse(Serialization.write(value)))))
}

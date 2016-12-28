package com.danielasfregola.twitter4s.util

import scala.io.Source
import org.json4s.native.Serialization
import com.danielasfregola.twitter4s.http.unmarshalling.{JsonSupport, OldJsonSupport}

trait FixturesSupport extends JsonSupport {

  def loadJson(resourcePath: String): String = Source.fromURL(getClass.getResource(resourcePath)).mkString
  def loadJsonAs[T: Manifest](resourcePath: String): T = readJsonAs[T](loadJson(resourcePath))

  def readJsonAs[T: Manifest](json: String): T = Serialization.read[T](json)
  def printAsJson[T <: AnyRef](value: T): Unit = println(Serialization.writePretty(value))
}

trait OldFixturesSupport extends OldJsonSupport {

  def loadJson(resourcePath: String): String = Source.fromURL(getClass.getResource(resourcePath)).mkString
  def loadJsonAs[T: Manifest](resourcePath: String): T = readJsonAs[T](loadJson(resourcePath))

  def readJsonAs[T: Manifest](json: String): T = Serialization.read[T](json)
  def printAsJson[T <: AnyRef](value: T): Unit = println(Serialization.writePretty(value))
}


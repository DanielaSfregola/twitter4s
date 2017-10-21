package com.danielasfregola.twitter4s.entities.enums

object FilterLevel extends Enumeration {
  type FilterLevel = Value

  val None = Value("none")
  val Low = Value("low")
  val Medium = Value("medium")
}

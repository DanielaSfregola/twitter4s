package com.danielasfregola.twitter4s.entities.enums

object AccessType extends Enumeration {
  type AccessType = Value

  val Read = Value("read")
  val Write = Value("write")
}

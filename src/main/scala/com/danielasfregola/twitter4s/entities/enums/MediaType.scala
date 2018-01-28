package com.danielasfregola.twitter4s.entities.enums

object MediaType extends Enumeration {
  type MediaType = Value

  val PNG = Value("image/png")
  val JPG = Value("image/jpeg")
  val GIF = Value("image/gif")
  val WEBP = Value("image/webp")

  val MP4 = Value("video/mp4")
  val MOV = Value("video/quicktime")
}

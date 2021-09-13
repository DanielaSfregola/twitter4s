package com.danielasfregola.twitter4s.entities.v2.enums

object MediaType extends Enumeration {
  type MediaType = Value

  val AnimatedGIF = Value("animated_gif")
  val Photo = Value("photo")
  val Video = Value("video")
}

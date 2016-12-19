package com.danielasfregola.twitter4s.entities

case class ProfileImage(mini: String, normal: String, bigger: String)

object ProfileImage {

  def apply(original: String): ProfileImage = {
    val mini = resize(original, "mini")
    val normal = resize(original, "normal")
    val big = resize(original, "bigger")
    apply(mini, normal, big)
  }

  private def resize(original: String, size: String): String = {
    val lastUnderscore = original.lastIndexOf('_')
    val lastDot = original.lastIndexOf('.')
    val prefix = original.substring(0, lastUnderscore +1)
    val suffix = original.substring(lastDot)
    s"$prefix$size$suffix"
  }
}

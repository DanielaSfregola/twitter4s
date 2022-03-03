package com.danielasfregola.twitter4s.entities

final case class ProfileImage(mini: String, normal: String, bigger: String, default: String)

object ProfileImage {
  private def isEmpty(x: String) = x == null || x.trim.isEmpty
  val empty: ProfileImage = ProfileImage(mini = "", normal = "", bigger = "", default = "")

  def apply(original: String): ProfileImage = {
    if (isEmpty(original)) {
      empty
    } else {
      val mini = resize(original, "_mini")
      val normal = resize(original, "_normal")
      val big = resize(original, "_bigger")
      val default = resize(original, "")
      apply(mini, normal, big, default)
    }
  }

  private def resize(original: String, size: String): String = {
    val lastUnderscore = original.lastIndexOf('_')
    val lastDot = original.lastIndexOf('.')
    val prefix = original.substring(0, lastUnderscore)
    val suffix = original.substring(lastDot)
    s"$prefix$size$suffix"
  }
}

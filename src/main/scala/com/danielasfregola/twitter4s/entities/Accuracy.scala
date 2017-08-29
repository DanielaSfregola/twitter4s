package com.danielasfregola.twitter4s.entities

import com.danielasfregola.twitter4s.entities.enums.Measure
import com.danielasfregola.twitter4s.entities.enums.Measure.Measure

final case class Accuracy(amount: Int, unit: Measure) {
  override def toString = s"$amount$unit"
}

object Accuracy {
  val Default = Accuracy(0, Measure.Meter)
}

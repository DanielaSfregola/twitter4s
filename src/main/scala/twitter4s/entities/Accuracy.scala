package twitter4s.entities

import twitter4s.entities.enums.Measure
import twitter4s.entities.enums.Measure.Measure

case class Accuracy(amount: Int, unit: Measure) {
  override def toString = s"$amount$unit"
}

object Accuracy {
  val Default = Accuracy(0, Measure.Meter)
}

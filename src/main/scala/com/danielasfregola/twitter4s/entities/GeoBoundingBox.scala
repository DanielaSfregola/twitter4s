package com.danielasfregola.twitter4s.entities

final case class GeoBoundingBox(southwest: Coordinate, northeast: Coordinate) {
  def toLngLatPairs: Seq[(Double, Double)] =
    Seq(southwest.toLngLatPair, northeast.toLngLatPair)
}

object GeoBoundingBox {
  def toLngLatPairs(boundingBoxes: Seq[GeoBoundingBox]): Seq[(Double, Double)] = {
    boundingBoxes.flatMap(_.toLngLatPairs)
  }
}

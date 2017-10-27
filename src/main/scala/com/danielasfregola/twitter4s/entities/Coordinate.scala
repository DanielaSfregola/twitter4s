package com.danielasfregola.twitter4s.entities

final case class Coordinate(longitude: Double, latitude: Double) {
  def toLngLatPair: (Double, Double) =
    (longitude, latitude)
}

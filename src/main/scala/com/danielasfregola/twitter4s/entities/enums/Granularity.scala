package com.danielasfregola.twitter4s.entities.enums

object Granularity extends Enumeration {
  type Granularity = Value

  val Admin = Value("admim")
  val City = Value("city")
  val Country = Value("country")
  val Neighborhood = Value("neighborhood")
  val Poi = Value("poi")
}

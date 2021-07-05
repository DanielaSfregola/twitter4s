package com.danielasfregola.twitter4s.entities.v2.enums.fields

object PlaceFields extends Enumeration {
  type PlaceFields = Value

  val ContainedWidth = Value("contained_within")
  val Country = Value("country")
  val CountryCode = Value("country_code")
  val FullName = Value("full_name")
  val Geo = Value("geo")
  val Id = Value("id")
  val Name = Value("name")
  val PlaceType = Value("place_type")
}

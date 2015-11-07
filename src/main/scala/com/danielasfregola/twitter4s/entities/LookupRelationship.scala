package com.danielasfregola.twitter4s.entities

case class LookupRelationship(connections: Seq[String] = Seq.empty,
                              id: Long,
                              id_str: String,
                              name: String,
                              screen_name: String)

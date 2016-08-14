package com.danielasfregola.twitter4s.entities.enums

object DisconnectionCode extends Enumeration {
  type DisconnectionCode = Value

  val Shutdown = Value(1)
  val DuplicateStream = Value(2)
  val ControlRequest = Value(3)
  val Stall = Value(4)
  val Normal = Value(5)
  val TokenRevoked = Value(6)
  val AdminLogout = Value(7)
  val Internal = Value(8)
  val MaxMessageLimit = Value(9)
  val StreamException = Value(10)
  val BrokerStall = Value(11)
  val ShedLoad = Value(12)

}

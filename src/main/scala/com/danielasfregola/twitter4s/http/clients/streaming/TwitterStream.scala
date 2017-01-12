package com.danielasfregola.twitter4s.http.clients.streaming

import akka.stream.SharedKillSwitch

class TwitterStream(private val killSwitch: SharedKillSwitch) {

  def close() = killSwitch.shutdown()

}

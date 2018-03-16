package com.danielasfregola.twitter4s.http.clients.streaming

object ErrorHandler {
  def default: PartialFunction[Throwable, Unit] = {
    case scala.util.control.NonFatal(e) => ()
  }
}

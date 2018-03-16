package com.danielasfregola.twitter4s.http.clients.streaming

object ErrorHandler {
  def ignore: PartialFunction[Throwable, Unit] = {
    case scala.util.control.NonFatal(e) => ()
  }
}

package com.danielasfregola.twitter4s.http.clients.streaming
import com.typesafe.scalalogging.LazyLogging

object ErrorHandler extends LazyLogging {
  def ignore: PartialFunction[Throwable, Unit] = {
    case scala.util.control.NonFatal(e) => logger.warn("Default error handling", e)
  }
}

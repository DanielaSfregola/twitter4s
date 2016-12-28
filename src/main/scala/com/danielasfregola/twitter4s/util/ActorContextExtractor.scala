package com.danielasfregola.twitter4s.util

import scala.concurrent.ExecutionContext
import akka.event.LoggingAdapter
import com.danielasfregola.twitter4s.providers.{ExecutionContextProvider, ActorSystemProvider}

trait ActorContextExtractor extends ExecutionContextProvider with ActorSystemProvider {

  implicit val log: LoggingAdapter = system.log
  implicit val executionContext: ExecutionContext = system.dispatcher

}

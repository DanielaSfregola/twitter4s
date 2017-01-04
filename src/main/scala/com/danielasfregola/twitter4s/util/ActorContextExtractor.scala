package com.danielasfregola.twitter4s.util

import akka.event.LoggingAdapter
import com.danielasfregola.twitter4s.providers.{ActorSystemProvider, ExecutionContextProvider}

import scala.concurrent.ExecutionContext

trait ActorContextExtractor extends ExecutionContextProvider with ActorSystemProvider {

  implicit val log: LoggingAdapter = system.log
  implicit val executionContext: ExecutionContext = system.dispatcher

}

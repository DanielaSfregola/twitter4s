package com.danielasfregola.twitter4s.util

import com.danielasfregola.twitter4s.providers.{ActorSystemProvider, ExecutionContextProvider}
import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext

trait ActorContextExtractor extends ExecutionContextProvider with ActorSystemProvider {

  implicit val log: Logger = Logger("twitter4s")
  implicit val executionContext: ExecutionContext = system.dispatcher

}

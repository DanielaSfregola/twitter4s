package com.danielasfregola.twitter4s.util

import scala.concurrent.ExecutionContext
import akka.event.LoggingAdapter

import spray.util.LoggingContext
import com.danielasfregola.twitter4s.providers.{ExecutionContextProvider, ActorRefFactoryProvider}

trait ActorContextExtractor extends ExecutionContextProvider with ActorRefFactoryProvider {

  implicit val log: LoggingAdapter = LoggingContext.fromActorRefFactory(actorRefFactory)
  implicit val executionContext: ExecutionContext = actorRefFactory.dispatcher

}

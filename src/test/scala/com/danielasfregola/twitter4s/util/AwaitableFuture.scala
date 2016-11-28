package com.danielasfregola.twitter4s.util

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.{ DurationInt, FiniteDuration }

trait AwaitableFuture {

  implicit class AwaitableFuture[T](f: Future[T]) {
    def await(implicit duration: FiniteDuration = 20 seconds) = Await.result(f, duration)
  }

}

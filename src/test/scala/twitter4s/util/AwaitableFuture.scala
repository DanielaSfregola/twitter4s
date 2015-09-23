package twitter4s.util

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.{ DurationInt, FiniteDuration }

trait AwaitableFuture {

  implicit class AwaitableFuture[T](f: Future[T]) {
    def await(implicit duration: FiniteDuration = 5 seconds) = Await.result(f, duration)
  }

}

package com.danielasfregola.twitter4s.helpers

trait TestExecutionContext {
  implicit val executionContext = scala.concurrent.ExecutionContext.Implicits.global
}

package com.danielasfregola.twitter4s.util

trait TestExecutionContext {
  implicit val executionContext = scala.concurrent.ExecutionContext.Implicits.global
}

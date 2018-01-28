package com.danielasfregola.twitter4s.providers

import scala.concurrent.ExecutionContext

private[twitter4s] trait ExecutionContextProvider {

  implicit def executionContext: ExecutionContext
}

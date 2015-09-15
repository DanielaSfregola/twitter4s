package twitter4s.providers

import scala.concurrent.ExecutionContext

trait ExecutionContextProvider {
  
  implicit def executionContext: ExecutionContext
}

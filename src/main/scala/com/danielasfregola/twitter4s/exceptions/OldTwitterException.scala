package com.danielasfregola.twitter4s.exceptions

import spray.http.StatusCode

case class OldTwitterException(code: StatusCode, errors: Errors) extends Exception(s"[$code] $errors")

object OldTwitterException {

  def apply(code: StatusCode, msg: String): OldTwitterException = {
    val error = TwitterError(msg, code.intValue)
    new OldTwitterException(code, Errors(error))
  }
}

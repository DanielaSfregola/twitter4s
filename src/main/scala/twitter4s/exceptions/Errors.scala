package twitter4s.exceptions

case class Errors(error: Seq[Error])

case class Error(message: String, code: Int)

class TwitterException(code: Int, message: String) extends Exception

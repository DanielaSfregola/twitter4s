package twitter4s.http.marshalling

abstract class Parameters extends Product with BodyEncoder {

  override def toString = toBodyAsEncodedParams(this)
}

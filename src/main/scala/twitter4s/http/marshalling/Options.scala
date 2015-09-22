package twitter4s.http.marshalling

abstract class Options extends Product with BodyEncoder {

  override def toString = toBodyAsParams(this)
}

package twitter4s.http.clients

import spray.client.pipelining._
import spray.http.HttpMethods._
import spray.http.{HttpMethod, HttpRequest}
import spray.httpx.unmarshalling.{Deserializer => _, FromResponseUnmarshaller}
import twitter4s.http.marshalling.BodyEncoder
import twitter4s.http.oauth.OAuthProvider
import twitter4s.providers.{ActorRefFactoryProvider, TokenProvider}

trait OAuthClient extends Client with TokenProvider with ActorRefFactoryProvider {

  lazy val oauthProvider = new OAuthProvider(consumerToken, accessToken)

  def pipeline[T: FromResponseUnmarshaller] =
    withOAuthHeader ~> logRequest ~> sendReceive ~> logResponse ~> unmarshal[T]

  def withOAuthHeader: HttpRequest => HttpRequest = { request =>
    val authorizationHeader = oauthProvider.oauthHeader(request)
    request.withHeaders( request.headers :+ authorizationHeader )
  }

  val Get = new OAuthRequestBuilder(GET)
  val Post = new OAuthRequestBuilder(POST)
  val Put = new OAuthRequestBuilder(PUT)
  val Patch = new OAuthRequestBuilder(PATCH)
  val Delete = new OAuthRequestBuilder(DELETE)
  val Options = new OAuthRequestBuilder(OPTIONS)
  val Head = new OAuthRequestBuilder(HEAD)

  class OAuthRequestBuilder(method: HttpMethod) extends RequestBuilder(method) with BodyEncoder {

    def apply(uri: String, content: Product): HttpRequest =
      apply(uri, toBodyAsParams(content))
  }

}



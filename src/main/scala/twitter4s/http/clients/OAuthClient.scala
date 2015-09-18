package twitter4s.http.clients

import akka.actor.ActorRefFactory

import spray.client.pipelining._
import spray.http.HttpRequest
import spray.httpx.unmarshalling.FromResponseUnmarshaller
import twitter4s.entities.{AccessToken, ConsumerToken}
import twitter4s.http.oauth.OAuthProvider

class OAuthClient(consumerToken: ConsumerToken, accessToken: AccessToken)
            (implicit val actorRefFactory: ActorRefFactory) extends Client {

  val oauthProvider = new OAuthProvider(consumerToken, accessToken)

  override def pipeline[T: FromResponseUnmarshaller] =
    withOAuthHeader ~> logRequest ~> sendReceive ~> logResponse ~> unmarshal[T]

  def withOAuthHeader: HttpRequest => HttpRequest = { request =>
    val authorizationHeader = oauthProvider.oauthHeader(request)
    request.withHeaders( request.headers :+ authorizationHeader )
  }

}

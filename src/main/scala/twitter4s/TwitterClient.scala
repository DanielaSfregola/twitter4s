package twitter4s

import akka.actor.ActorRefFactory
import twitter4s.entities.AccessToken._
import twitter4s.entities.ConsumerToken._
import twitter4s.entities.{AccessToken, ConsumerToken}
import twitter4s.http.clients.OAuthClient
import twitter4s.statuses.TwitterStatusClient

class TwitterClient(consumerToken: ConsumerToken, accessToken: AccessToken)
                   (implicit actorRefFactory: ActorRefFactory) extends OAuthClient(consumerToken, accessToken)
                    with TwitterStatusClient

object TwitterClient {

  def apply(consumerToken: ConsumerToken = ConsumerTokenFromConf, accessToken: AccessToken = AccessTokenFromConf)
           (implicit actorRefFactory: ActorRefFactory): TwitterClient =
    new TwitterClient(consumerToken, accessToken)

  implicit def toOption[T](t: T): Option[T] = Some(t)
}

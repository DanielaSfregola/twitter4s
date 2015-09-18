package twitter4s

import akka.actor.ActorRefFactory
import twitter4s.entities.AccessToken._
import twitter4s.entities.ConsumerToken._
import twitter4s.entities.{AccessToken, ConsumerToken}
import twitter4s.http.clients.OAuthClient

class TwitterClient(consumerToken: ConsumerToken = ConsumerTokenFromConf, accessToken: AccessToken = AccessTokenFromConf)
                   (implicit actorRefFactory: ActorRefFactory) extends OAuthClient(consumerToken, accessToken) {


}


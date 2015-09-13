package twitter4s

import akka.actor.ActorRefFactory

import twitter4s.entities.AccessToken._
import twitter4s.entities.ConsumerToken._
import twitter4s.entities.{AccessToken, ConsumerToken}
import twitter4s.utils.ActorContextExtractor

class TwitterClient(val consumerToken: ConsumerToken = ConsumerTokenFromConf,
                    val accessToken: AccessToken = AccessTokenFromConf)(implicit val actorRefFactory: ActorRefFactory) extends ActorContextExtractor {


}

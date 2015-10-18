package twitter4s.http.clients.application

import scala.concurrent.Future

import twitter4s.entities.RateLimits
import twitter4s.entities.enums.Resource.Resource
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.application.parameters.RatesParameters
import twitter4s.util.Configurations

trait TwitterApplicationClient extends OAuthClient with Configurations {

  val applicationUrl = s"$apiTwitterUrl/$twitterVersion/application"

  def rateLimits(resources: Resource*): Future[RateLimits] = {
    val parameters = RatesParameters(Option(resources.mkString(",")).filter(_.trim.nonEmpty))
    Get(s"$applicationUrl/rate_limit_status.json", parameters).respondAs[RateLimits]
  }

}

package com.danielasfregola.twitter4s.http.clients.rest.application

import com.danielasfregola.twitter4s.entities.enums.Resource.Resource
import com.danielasfregola.twitter4s.entities.{RateLimits, RatedData}
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.application.parameters.RatesParameters
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `application` resource.
  */
trait TwitterApplicationClient {

  protected val restClient: RestClient

  private val applicationUrl = s"$apiTwitterUrl/$twitterVersion/application"

  /** Returns the current rate limits for methods belonging to the specified resource families.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/developer-utilities/rate-limit-status/api-reference/get-application-rate_limit_status" target="_blank">
    *   https://developer.twitter.com/en/docs/developer-utilities/rate-limit-status/api-reference/get-application-rate_limit_status</a>.
    *
    * @param resources : A comma-separated list of resource families you want to know the current rate limit disposition for.
    *                  If no resources are specified, all the resources are considered.
    * @return : The current rate limits for methods belonging to the specified resource families.
    */
  def rateLimits(resources: Resource*): Future[RatedData[RateLimits]] = {
    import restClient._
    val parameters = RatesParameters(Option(resources.mkString(",")).filter(_.trim.nonEmpty))
    Get(s"$applicationUrl/rate_limit_status.json", parameters).respondAsRated[RateLimits]
  }

}

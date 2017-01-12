package com.danielasfregola.twitter4s.http.clients.rest.application

import com.danielasfregola.twitter4s.entities.RateLimits
import com.danielasfregola.twitter4s.entities.enums.Resource.Resource
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.application.parameters.RatesParameters
import com.danielasfregola.twitter4s.util.Configurations

import scala.concurrent.Future

/** Implements the available requests for the `application` resource.
  */
trait TwitterApplicationClient extends RestClient with Configurations {

  private val applicationUrl = s"$apiTwitterUrl/$twitterVersion/application"

  /** Returns the current rate limits for methods belonging to the specified resource families.
    * For more information see
    * <a href="https://dev.twitter.com/rest/reference/get/application/rate_limit_status" target="_blank">
    *   https://dev.twitter.com/rest/reference/get/application/rate_limit_status</a>.
    *
    * @param resources : A comma-separated list of resource families you want to know the current rate limit disposition for.
    *                  If no resources are specified, all the resources are considered.
    * @return : The current rate limits for methods belonging to the specified resource families.
    */
  def rateLimits(resources: Resource*): Future[RateLimits] = {
    val parameters = RatesParameters(Option(resources.mkString(",")).filter(_.trim.nonEmpty))
    Get(s"$applicationUrl/rate_limit_status.json", parameters).respondAs[RateLimits]
  }

  @deprecated("use rateLimits instead", "2.2")
  def getRateLimits(resources: Resource*): Future[RateLimits] =
    rateLimits(resources:_*)

}

package com.danielasfregola.twitter4s.http.clients.application

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.RateLimits
import com.danielasfregola.twitter4s.entities.enums.Resource.Resource
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.application.parameters.RatesParameters
import com.danielasfregola.twitter4s.util.Configurations

/** Implements the available requests for the `application` resource.
  */
trait TwitterApplicationClient extends OAuthClient with Configurations {

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
  def getRateLimits(resources: Resource*): Future[RateLimits] = {
    val parameters = RatesParameters(Option(resources.mkString(",")).filter(_.trim.nonEmpty))
    Get(s"$applicationUrl/rate_limit_status.json", parameters).respondAs[RateLimits]
  }

}

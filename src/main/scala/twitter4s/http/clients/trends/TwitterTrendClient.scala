package twitter4s.http.clients.trends

import scala.concurrent.Future

import twitter4s.entities.{Location, LocationTrends}
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.trends.parameters.TrendsParameters
import twitter4s.util.Configurations

trait TwitterTrendClient extends OAuthClient with Configurations {

  val trendsUrl = s"$apiTwitterUrl/$twitterVersion/trends"

  def globalTrends(exclude_hashtags: Boolean = false): Future[LocationTrends] = trends(1, exclude_hashtags)

  def trends(woeid: Long, exclude_hashtags: Boolean = false): Future[LocationTrends] = {
    val exclude = if (exclude_hashtags) Some("hashtags") else None
    val parameters = TrendsParameters(woeid, exclude)
    Get(s"$trendsUrl/place.json", parameters).respondAs[LocationTrends]
  }

  def availableLocationTrends(): Future[Seq[Location]] =
    Get(s"$trendsUrl/available.json").respondAs[Seq[Location]]

}

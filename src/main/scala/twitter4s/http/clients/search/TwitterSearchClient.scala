package twitter4s.http.clients.search

import java.time.LocalDate

import scala.concurrent.Future

import twitter4s.entities.{StatusSearch, GeoCode}
import twitter4s.entities.enums.Language._
import twitter4s.entities.enums.ResultType
import twitter4s.entities.enums.ResultType._
import twitter4s.http.clients.OAuthClient
import twitter4s.http.clients.search.parameters.TweetSearchParameters
import twitter4s.util.Configurations

trait TwitterSearchClient extends OAuthClient with Configurations {

  private val searchUrl = s"$apiTwitterUrl/$twitterVersion/search"

  def searchTweet(query: String,
                  count: Int = 15,
                  include_entities: Boolean = true,
                  result_type: ResultType = ResultType.Mixed,
                  geocode: Option[GeoCode] = None,
                  language: Option[Language] = None,
                  locale: Option[String] = None,
                  until: Option[LocalDate] = None,
                  since_id: Option[Long] = None,
                  max_id: Option[Long] = None,
                  callback: Option[String] = None): Future[StatusSearch] = {
    val parameters = TweetSearchParameters(query, count, include_entities, result_type, geocode, language, locale, until, since_id, max_id, callback)
    Get(s"$searchUrl/tweets.json", parameters).respondAs[StatusSearch]
  }

  // TODO - GET saved_searches/list
  // TODO - GET saved_searches/show/:id
  // TODO - POST saved_searches/create
  // TODO - POST saved_searches/destroy/:id
}

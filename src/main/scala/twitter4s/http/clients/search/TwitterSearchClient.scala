package twitter4s.http.clients.search

import twitter4s.http.clients.OAuthClient
import twitter4s.util.Configurations

trait TwitterSearchClient extends OAuthClient with Configurations {

  val helpUrl = s"$apiTwitterUrl/$twitterVersion/search"

  // TODO - GET search/tweets

  // TODO - GET saved_searches/list
  // TODO - GET saved_searches/show/:id
  // TODO - POST saved_searches/create
  // TODO - POST saved_searches/destroy/:id
}

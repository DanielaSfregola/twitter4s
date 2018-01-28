package com.danielasfregola.twitter4s.entities.streaming.user

import com.danielasfregola.twitter4s.entities.streaming.UserStreamingMessage

/** Upon establishing a User Stream connection, Twitter will send a preamble before starting regular message delivery.
  * This preamble contains a list of the user’s friends. This is represented as an array of user ids as longs.
  * For more information see <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types" target="_blank">
  *   https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types</a>
  */
final case class FriendsLists(friends: Seq[Long]) extends UserStreamingMessage

/** Upon establishing a User Stream connection, Twitter will send a preamble before starting regular message delivery.
  * This preamble contains a list of the user’s friends. This is represented as an array of user ids as strings.
  * For more information see <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types" target="_blank">
  *   https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types</a>
  */
final case class FriendsListsStringified(friends: Seq[String]) extends UserStreamingMessage

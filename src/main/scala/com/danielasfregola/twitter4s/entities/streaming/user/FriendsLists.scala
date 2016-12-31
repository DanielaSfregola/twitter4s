package com.danielasfregola.twitter4s.entities.streaming.user

import com.danielasfregola.twitter4s.entities.streaming.UserStreamingMessage

/** Upon establishing a User Stream connection, Twitter will send a preamble before starting regular message delivery.
  * This preamble contains a list of the user’s friends. This is represented as an array of user ids as longs.
  * For more information see <a href="https://dev.twitter.com/streaming/overview/messages-types#friends_list_friends" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#friends_list_friends</a>
  */
case class FriendsLists(friends: Seq[Long]) extends UserStreamingMessage

/** Upon establishing a User Stream connection, Twitter will send a preamble before starting regular message delivery.
  * This preamble contains a list of the user’s friends. This is represented as an array of user ids as strings.
  * For more information see <a href="https://dev.twitter.com/streaming/overview/messages-types#friends_list_friends" target="_blank">
  *   https://dev.twitter.com/streaming/overview/messages-types#friends_list_friends</a>
  */
case class FriendsListsStringified(friends: Seq[String]) extends UserStreamingMessage



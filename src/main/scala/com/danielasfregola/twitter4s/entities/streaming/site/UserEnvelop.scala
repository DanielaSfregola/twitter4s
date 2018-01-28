package com.danielasfregola.twitter4s.entities.streaming.site

import com.danielasfregola.twitter4s.entities.streaming._
import com.danielasfregola.twitter4s.entities.streaming.common.WarningMessage
import com.danielasfregola.twitter4s.entities.streaming.user._
import com.danielasfregola.twitter4s.entities.{DirectMessage, Tweet}

/** Site Streams are sent the same messages as User Streams (including friends lists in the preamble),
  * but for multiple users instead of a single user.The same types of messages are streamed, but to identify the target of each message,
  * an additional wrapper is placed around every message, except for blank keep-alive lines.
  * For more information see <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types" target="_blank">
  *   https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types</a>
  */
abstract class UserEnvelop[T <: StreamingMessage](for_user: Long, message: T) extends SiteStreamingMessage

final case class UserEnvelopTweet(for_user: Long, message: Tweet) extends UserEnvelop(for_user, message)

final case class UserEnvelopDirectMessage(for_user: Long, message: DirectMessage) extends UserEnvelop(for_user, message)

final case class UserEnvelopSimpleEvent(for_user: Long, message: SimpleEvent) extends UserEnvelop(for_user, message)

final case class UserEnvelopTweetEvent(for_user: Long, message: TweetEvent) extends UserEnvelop(for_user, message)

final case class UserEnvelopTwitterListEvent(for_user: Long, message: TwitterListEvent)
    extends UserEnvelop(for_user, message)

final case class UserEnvelopFriendsLists(for_user: Long, message: FriendsLists) extends UserEnvelop(for_user, message)

final case class UserEnvelopWarningMessage(for_user: Long, message: WarningMessage)
    extends UserEnvelop(for_user, message)

/** Site Streams are sent the same messages as User Streams (including friends lists in the preamble),
  * but for multiple users instead of a single user.The same types of messages are streamed, but to identify the target of each message,
  * an additional wrapper is placed around every message, except for blank keep-alive lines.
  * For more information see <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types" target="_blank">
  *   https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types</a>
  */
abstract class UserEnvelopStringified[T <: StreamingMessage](for_user: String, message: StreamingMessage)
    extends SiteStreamingMessage

final case class UserEnvelopTweetStringified(for_user: String, message: Tweet)
    extends UserEnvelopStringified[Tweet](for_user, message)

final case class UserEnvelopDirectMessageStringified(for_user: String, message: DirectMessage)
    extends UserEnvelopStringified[DirectMessage](for_user, message)

final case class UserEnvelopSimpleEventStringified(for_user: String, message: SimpleEvent)
    extends UserEnvelopStringified[SimpleEvent](for_user, message)

final case class UserEnvelopTweetEventStringified(for_user: String, message: TweetEvent)
    extends UserEnvelopStringified[TweetEvent](for_user, message)

final case class UserEnvelopTwitterListEventStringified(for_user: String, message: TwitterListEvent)
    extends UserEnvelopStringified[TwitterListEvent](for_user, message)

final case class UserEnvelopFriendsListsStringified(for_user: String, message: FriendsLists)
    extends UserEnvelopStringified[FriendsLists](for_user, message)

final case class UserEnvelopWarningMessageStringified(for_user: String, message: WarningMessage)
    extends UserEnvelopStringified[WarningMessage](for_user, message)

package com.danielasfregola.twitter4s.entities.streaming.user

import java.time.Instant

import com.danielasfregola.twitter4s.entities.enums.EventCode
import com.danielasfregola.twitter4s.entities.enums.SimpleEventCode.SimpleEventCode
import com.danielasfregola.twitter4s.entities.enums.TweetEventCode.TweetEventCode
import com.danielasfregola.twitter4s.entities.enums.TwitterListEventCode.TwitterListEventCode
import com.danielasfregola.twitter4s.entities.streaming.UserStreamingMessage
import com.danielasfregola.twitter4s.entities.{Tweet, TwitterList, User}

/** Notifications about non-Tweet events are also sent over a user stream.
  * The values present will be different based on the type of event.
  * For more information see
  * <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types" target="_blank">
  *   https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/streaming-message-types</a>.
  */
abstract class Event[T](created_at: Instant,
                        event: EventCode#Value,
                        target: User,
                        source: User,
                        target_object: Option[T])
    extends UserStreamingMessage

final case class SimpleEvent(created_at: Instant,
                             event: SimpleEventCode,
                             target: User,
                             source: User,
                             target_object: Option[String])
    extends Event(created_at, event, target, source, target_object)

final case class TweetEvent(created_at: Instant,
                            event: TweetEventCode,
                            target: User,
                            source: User,
                            target_object: Tweet)
    extends Event(created_at, event, target, source, Some(target_object))

final case class TwitterListEvent(created_at: Instant,
                                  event: TwitterListEventCode,
                                  target: User,
                                  source: User,
                                  target_object: TwitterList)
    extends Event(created_at, event, target, source, Some(target_object))

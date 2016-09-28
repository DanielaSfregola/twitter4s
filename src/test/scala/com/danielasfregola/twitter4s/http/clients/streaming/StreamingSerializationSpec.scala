package com.danielasfregola.twitter4s.http.clients.streaming

import com.danielasfregola.twitter4s.entities.{DirectMessage, Tweet}
import com.danielasfregola.twitter4s.entities.streaming._
import com.danielasfregola.twitter4s.entities.streaming.common._
import com.danielasfregola.twitter4s.entities.streaming.site.{ControlMessage, UserEnvelopFriendsLists, UserEnvelopFriendsListsStringified}
import com.danielasfregola.twitter4s.entities.streaming.user.{FriendsLists, FriendsListsStringified, TweetEvent}
import com.danielasfregola.twitter4s.util.ClientSpec
import spray.http._

import scala.io.Source

class StreamingSerializationSpec extends ClientSpec {

  "StreamingActor" should {

    "parse a stream of data and output the corresponding entities of type" in {

      "Tweet" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/common/tweets.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[Tweet]]("/fixtures/streaming/common/tweets.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "DirectMessage" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/user/direct_messages.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[DirectMessage]]("/fixtures/streaming/user/direct_messages.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "ControlMessage" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/site/control_messages.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[ControlMessage]]("/fixtures/streaming/site/control_messages.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "DisconnectMessage" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/common/disconnect_messages.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[DisconnectMessage]]("/fixtures/streaming/common/disconnect_messages.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "TweetEvent" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/user/events.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[TweetEvent]]("/fixtures/streaming/user/events.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "FriendsLists" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/user/friends_lists.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[FriendsLists]]("/fixtures/streaming/user/friends_lists.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "FriendsListsStringified" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/user/friends_lists_stringified.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[FriendsListsStringified]]("/fixtures/streaming/user/friends_lists_stringified.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "LimitNotice" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/common/limit_notices.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[LimitNotice]]("/fixtures/streaming/common/limit_notices.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "LocationDeletionNotice" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/common/location_deletion_notices.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[LocationDeletionNotice]]("/fixtures/streaming/common/location_deletion_notices.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "StatusDeletionNotice" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/common/status_deletion_notices.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[StatusDeletionNotice]]("/fixtures/streaming/common/status_deletion_notices.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "StatusWithheldNotice" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/common/status_withheld_notices.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[StatusWithheldNotice]]("/fixtures/streaming/common/status_withheld_notices.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "UserEnvelopFriendsLists" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/site/user_envelops.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[UserEnvelopFriendsLists]]("/fixtures/streaming/site/user_envelops.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "UserEnvelopFriendsListsStringified" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/site/user_envelops_stringified.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[UserEnvelopFriendsListsStringified]]("/fixtures/streaming/site/user_envelops_stringified.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "UserWithheldNotice" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/common/user_withheld_notices.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[UserWithheldNotice]]("/fixtures/streaming/common/user_withheld_notices.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "WarningMessage" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/common/warning_messages.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[WarningMessage]]("/fixtures/streaming/common/warning_messages.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }
    }
  }
}

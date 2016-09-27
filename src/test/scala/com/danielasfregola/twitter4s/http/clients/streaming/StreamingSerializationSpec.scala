package com.danielasfregola.twitter4s.http.clients.streaming

import com.danielasfregola.twitter4s.entities.enums.EventCode.EventCode
import com.danielasfregola.twitter4s.entities.streaming._
import com.danielasfregola.twitter4s.entities.{DirectMessage, Tweet, TwitterList}
import com.danielasfregola.twitter4s.util.ClientSpec
import spray.http._

import scala.io.Source

class StreamingSerializationSpec extends ClientSpec {

  "StreamingActor" should {

    "parse a stream of data and output the corresponding entities of type" in {

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

        Source.fromURL(getClass.getResource("/twitter/streaming/public/disconnect_messages.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[DisconnectMessage]]("/fixtures/streaming/public/disconnect_messages.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "EventCode" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/user/events.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[Event]]("/fixtures/streaming/user/events.json").map(StreamingUpdate(_))

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

        Source.fromURL(getClass.getResource("/twitter/streaming/public/limit_notices.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[LimitNotice]]("/fixtures/streaming/public/limit_notices.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "LocationDeletionNotice" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/public/location_deletion_notices.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[LocationDeletionNotice]]("/fixtures/streaming/public/location_deletion_notices.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "StatusDeletionNotice" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/public/status_deletion_notices.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[StatusDeletionNotice]]("/fixtures/streaming/public/status_deletion_notices.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "StatusWithheldNotice" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/public/status_withheld_notices.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[StatusWithheldNotice]]("/fixtures/streaming/public/status_withheld_notices.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      // TODO - fix!
//      "UserEnvelop" in new TwitterStreamingSpecContext {
//        val processor = system.actorOf(StreamingActor.props(self))
//
//        Source.fromURL(getClass.getResource("/twitter/streaming/site/user_envelops.json")).getLines.foreach { line =>
//          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
//        }
//
//        val messages: Seq[StreamingUpdate] =
//          loadJsonAs[Seq[UserEnvelop]]("/fixtures/streaming/site/user_envelops.json").map(StreamingUpdate(_))
//
//        expectMsgAllOf(messages: _*)
//      }

      "UserWithheldNotice" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/public/user_withheld_notices.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[UserWithheldNotice]]("/fixtures/streaming/public/user_withheld_notices.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }

      "WarningMessage" in new TwitterStreamingSpecContext {
        val processor = system.actorOf(StreamingActor.props(self))

        Source.fromURL(getClass.getResource("/twitter/streaming/public/warning_messages.json")).getLines.foreach { line =>
          processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
        }

        val messages: Seq[StreamingUpdate] =
          loadJsonAs[Seq[WarningMessage]]("/fixtures/streaming/public/warning_messages.json").map(StreamingUpdate(_))

        expectMsgAllOf(messages: _*)
      }
    }
  }
}

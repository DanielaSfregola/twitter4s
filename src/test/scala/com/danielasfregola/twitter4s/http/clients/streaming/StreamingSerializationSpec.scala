package com.danielasfregola.twitter4s.http.clients.streaming

import com.danielasfregola.twitter4s.entities.streaming._
import com.danielasfregola.twitter4s.entities.Tweet
import com.danielasfregola.twitter4s.util.ClientSpec
import spray.http._

import scala.io.Source

class StreamingSerializationSpec extends ClientSpec {

  class TwitterStatusClientSpecContext extends TwitterStreamingSpecContext

  "StreamingActor" should {

    "parse a stream of data and output the corresponding entities" in new TwitterStatusClientSpecContext {
      val processor = system.actorOf(StreamingActor.props(self))

      Source.fromURL(getClass.getResource("/fixtures/streaming/public.json")).getLines.foreach { line =>
        processor ! MessageChunk(HttpData(line) +: HttpData("\r\n"))
      }

      val messages: Seq[StreamingUpdate] =
        (loadJsonAs[Seq[ControlMessage]]("/twitter/streaming/public_control_messages.json") ++
        loadJsonAs[Seq[DisconnectMessage]]("/twitter/streaming/public_disconnected_messages.json") ++
        loadJsonAs[Seq[Tweet]]("/twitter/streaming/public_tweets.json") ++
        loadJsonAs[Seq[FriendsLists]]("/twitter/streaming/public_friends_lists.json") ++
        loadJsonAs[Seq[FriendsListsStringified]]("/twitter/streaming/public_friends_lists_stringified.json") ++
        loadJsonAs[Seq[LimitNotice]]("/twitter/streaming/public_limit_notices.json") ++
        loadJsonAs[Seq[LocationDeletionNotice]]("/twitter/streaming/public_location_deletion_notices.json") ++
        loadJsonAs[Seq[StatusDeletionNotice]]("/twitter/streaming/public_status_deletion_notices.json") ++
        loadJsonAs[Seq[StatusWithheldNotice]]("/twitter/streaming/public_status_withheld_notices.json") ++
        loadJsonAs[Seq[UserWithheldNotice]]("/twitter/streaming/public_user_withheld_notices.json") ++
        loadJsonAs[Seq[WarningMessage]]("/twitter/streaming/public_warning_messages.json"))
        .map (StreamingUpdate(_))

      expectMsgAllOf(messages: _*)
    }
  }
}

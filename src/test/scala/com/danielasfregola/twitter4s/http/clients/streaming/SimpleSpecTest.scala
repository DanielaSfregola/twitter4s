package com.danielasfregola.twitter4s.http.clients.streaming
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.entities.streaming.common.{WarningMessage, WarningMessageInfo}
import com.danielasfregola.twitter4s.helpers.ClientSpec
import com.danielasfregola.twitter4s.http.serializers.{JsonSupport, StreamingMessageFormats}
import org.json4s.native.Serialization

class SimpleSpecTest extends ClientSpec with JsonSupport {

  implicit val formats = StreamingMessageFormats // Brings in default date formats etc.

  "StreamingClient" should {

    "messages should serialize and deserialize" in {
      val warningMessage = WarningMessage(WarningMessageInfo("XX", "msg", Some(3), Some(1)))
      val a = Serialization.write(warningMessage)
      val qq = Serialization.read[StreamingMessage](a)
      qq must be(warningMessage)
    }
  }
}

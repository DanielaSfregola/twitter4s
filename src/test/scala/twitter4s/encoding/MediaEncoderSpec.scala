package twitter4s.encoding

import java.io.FileInputStream

import org.specs2.mutable.Specification

class MediaEncoderSpec extends Specification {

  trait MediaEncoderSpecContext extends MediaEncoder {
    val chunkSize = 1

    def test(chunk: Chunk, idx: Int) = {
      println(s"[$idx] >> $chunk")
      true
    }
  }

  "Media Encoder" should {

    "process an input stream in chunk" in new MediaEncoderSpecContext {
      val fileInputStream = new FileInputStream("/twitter.png")
      processAsChunks(fileInputStream, test)
    }
  }
}

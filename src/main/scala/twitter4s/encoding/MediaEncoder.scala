package twitter4s.encoding

import java.io.InputStream

trait MediaEncoder {

  def readAsChuncks(inputStream: InputStream, chunckSize: Int): Stream[Byte] =
    Stream.continually(inputStream.read).take(chunckSize).map(_.toByte)

}

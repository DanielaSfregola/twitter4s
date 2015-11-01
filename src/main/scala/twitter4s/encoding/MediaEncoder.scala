package twitter4s.encoding

import java.io.InputStream
import java.util.Base64

case class Chunk(base64Data: String)

trait MediaEncoder {

  protected val chunkSize: Int

  def processAsChunks[T](inputStream: InputStream, f: (Chunk, Int) => T): Seq[T] = {
    val chunks = readChunks(inputStream).zipWithIndex
    chunks.map{ case (chunk, idx) => f(chunk, idx) }
  }

  private def readChunks(inputStream: InputStream): Stream[Chunk] = {
    val bytes = Array.fill[Byte](chunkSize)(0)
    val length = inputStream.read(bytes)
    if (length <= 0) Stream()
    else {
      val base64Data = Base64.getEncoder.encodeToString(bytes.take(length))
      Chunk(base64Data) #:: readChunks(inputStream)
    }
  }
}

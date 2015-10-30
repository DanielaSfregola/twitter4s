package twitter4s.encoding

import java.io.InputStream

case class Chunk(length: Int, bytes: Array[Byte])

trait MediaEncoder {

  val chunkSize: Int

  def processAsChunks[T](inputStream: InputStream, f: (Chunk, Int) => T): Stream[T] = {
    val chunks = readChunks(inputStream).zipWithIndex
    chunks.map{ case (chunk, idx) => f(chunk, idx) }
  }

  private def readChunks(inputStream: InputStream): Stream[Chunk] = {
    val bytes = Array.fill[Byte](chunkSize)(0)
    val length = inputStream.read(bytes)
    Chunk(length, bytes) #:: readChunks(inputStream)
  }
}

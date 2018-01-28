package com.danielasfregola.twitter4s.util

import java.io.InputStream

private[twitter4s] final case class Chunk(base64Data: Seq[String])

private[twitter4s] class MediaReader(val chunkSize: Int) extends Encoder {

  def processAsChunks[T](inputStream: InputStream, f: (Chunk, Int) => T): Seq[T] = {
    val chunks = readChunks(inputStream).zipWithIndex
    chunks.map { case (chunk, idx) => f(chunk, idx) }
  }

  private def readChunks(inputStream: InputStream): Stream[Chunk] = {
    val bytes = Array.fill[Byte](chunkSize)(0)
    val length = inputStream.read(bytes)
    if (length <= 0) Stream()
    else {
      val base64Data = toBase64(bytes.take(length))
      Chunk(base64Data.grouped(60).toList) #:: readChunks(inputStream)
    }
  }
}

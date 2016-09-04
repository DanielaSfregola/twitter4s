package com.danielasfregola.twitter4s.util

import java.io.InputStream


private[twitter4s] case class Chunk(base64Data: Seq[String])

trait MediaReader extends Encoder {

  protected val chunkSize: Int

  private[twitter4s] def processAsChunks[T](inputStream: InputStream, f: (Chunk, Int) => T): Seq[T] = {
    val chunks = readChunks(inputStream).zipWithIndex
    chunks.map{ case (chunk, idx) => f(chunk, idx) }
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

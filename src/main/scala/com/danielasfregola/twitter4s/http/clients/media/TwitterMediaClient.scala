package com.danielasfregola.twitter4s.http
package clients.media

import java.io.{File, FileInputStream, InputStream}
import java.net.URLConnection

import com.danielasfregola.twitter4s.entities.MediaDetails
import com.danielasfregola.twitter4s.http.clients.MediaOAuthClient

import scala.concurrent.Future
import spray.http._
import com.danielasfregola.twitter4s.http.clients.media.entities._
import com.danielasfregola.twitter4s.util.{Chunk, Configurations, MediaReader}

trait TwitterMediaClient extends MediaOAuthClient with MediaReader with Configurations {

  private val mediaUrl = s"$mediaTwitterUrl/$twitterVersion/media"

  protected val chunkSize = 5 * 1024 * 1024   // 5 MB

  // TODO media_type enum
  def uploadMedia(filePath: String, additional_owners: Seq[String] = Seq.empty): Future[MediaDetails] = {
    uploadMediaFromFile(new File(filePath), additional_owners)
  }
  
  def uploadMediaFromFile(file: File, additional_owners: Seq[String] = Seq.empty): Future[MediaDetails] = {
    val size = file.length
    val inputStream = new FileInputStream(file)
    val filename = file.getName
    val mediaType = URLConnection.guessContentTypeFromName(filename)
    uploadMediaFromInputStream(inputStream, size, mediaType, Some(filename), additional_owners)
  }

  def uploadMediaFromInputStream(inputStream: InputStream, size: Long, media_type: String, filename: Option[String] = None, additional_owners: Seq[String] = Seq.empty): Future[MediaDetails] = {

    def filenameBuilder(mediaId: Long) = {
      val extension = media_type.split("/", 2)
      filename.getOrElse(s"twitter4s-$mediaId.$extension")
    }

    for {
      details <- initMedia(size, media_type, additional_owners)
      uploads <- appendMedia(details.media_id, inputStream, filenameBuilder(details.media_id))
      finalize <- finalizeMedia(details.media_id)
    } yield finalize
  }

  private def initMedia(size: Long,
                        media_type: String,
                        additional_owners: Seq[String]): Future[InitMediaDetails] = {
    val parameters = MediaInitParameters(size, media_type.toAscii, Some(additional_owners.mkString(",")))
    Post(s"$mediaUrl/upload.json", parameters).respondAs[InitMediaDetails]
  }

  private def appendMedia(mediaId: Long, inputStream: InputStream, filename: String): Future[Seq[Unit]] = {
    val appendMediaById = appendMediaChunk(mediaId, filename)_
    Future.sequence(processAsChunks(inputStream, appendMediaById))
  }

  private def appendMediaChunk(mediaId: Long, filename: String)(chunk: Chunk, idx: Int): Future[Unit] = {
    val fields: Map[String, BodyPart] = Map(
      "command" -> BodyPart("APPEND"),
      "media_id" -> BodyPart(mediaId.toString),
      "segment_index" -> BodyPart(idx.toString),
      "media_data" -> BodyPart(chunk.base64Data.mkString)
    )

    formDataPipeline apply Post(s"$mediaUrl/upload.json", MultipartFormData(fields))
  }

  private def finalizeMedia(mediaId: Long): Future[MediaDetails] = {
    val entity = MediaFinalize(mediaId)
    Post(s"$mediaUrl/upload.json", entity).respondAs[MediaDetails]
  }

  // TODO - fix me!
  def statusMedia(mediaId: Long): Future[Unit] = {
    val entity = MediaStatus(mediaId)
    Get(s"$mediaUrl/upload.json", entity).respondAs[Unit]
  }
}

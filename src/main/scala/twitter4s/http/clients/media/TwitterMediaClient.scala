package twitter4s.http
package clients.media

import java.io.{File, FileInputStream, InputStream}
import java.net.URLConnection
import scala.concurrent.Future

import spray.http._
import twitter4s.encoding.Chunk
import twitter4s.entities.MediaDetails
import twitter4s.http.clients.MediaOAuthClient
import twitter4s.http.clients.media.entities._
import twitter4s.util.{Chunk, MediaReader, Configurations}

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
    val CRLF = """\r\n"""
    val boundary = s"""00t4s${mediaId}s4t99"""
    val fieldName = "media"

    def bodyBuilder(key: String, value: String) = "--" + boundary +
      s"""${CRLF}Content-Disposition: form-data; name=\"$key\"$CRLF""" +
      s"""$CRLF$value$CRLF"""

    val bodyFileInformation = "--" + boundary +
     s"""${CRLF}Content-Disposition: form-data; name=\"$fieldName\"; filename=\"$filename\"$CRLF""" +
     s"""Content-Type: application/octet-stream$CRLF""" +
     s"""Content-Transfer-Encoding: base64$CRLF"""

    val encodedData = s"""$CRLF${chunk.base64Data.mkString("""\n""")}\\n"""

    val body = bodyBuilder("command", "APPEND") +
               bodyBuilder("media_id", mediaId.toString) +
               bodyBuilder("segment_index", idx.toString) +
               bodyFileInformation +
               encodedData +
               s"""$CRLF--$boundary--$CRLF"""

    Post(s"$mediaUrl/upload.json", body,
          ContentType(MediaTypes.`multipart/form-data` withBoundary(boundary))
        ).respondAs[Unit]
  }

  private def finalizeMedia(mediaId: Long): Future[MediaDetails] = {
    val entity = MediaFinalize(mediaId)
    Post(s"$mediaUrl/upload.json", entity).respondAs[MediaDetails]
  }

  def statusMedia(mediaId: Long): Future[Unit] = { // TODO - change type
    val entity = MediaStatus(mediaId)
    Post(s"$mediaUrl/upload.json", entity).respondAs[Unit]
  }
}

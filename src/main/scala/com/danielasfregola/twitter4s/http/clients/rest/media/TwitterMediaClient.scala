package com.danielasfregola.twitter4s.http
package clients.rest.media

import java.io.{File, FileInputStream, InputStream}
import java.net.URLConnection

import akka.http.scaladsl.model.Multipart._
import akka.http.scaladsl.model.{ContentTypes, MediaType}
import akka.stream.scaladsl.Source
import com.danielasfregola.twitter4s.entities.MediaDetails
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.media.parameters._
import com.danielasfregola.twitter4s.util.Configurations._
import com.danielasfregola.twitter4s.util.{Chunk, MediaReader}
import org.json4s.native.Serialization

import scala.concurrent.{ExecutionContext, Future}

/** Implements the available endpoints for the MEDIA API.
  */
trait TwitterMediaClient {

  protected val restClient: RestClient

  private val mediaUrl = s"$mediaTwitterUrl/$twitterVersion/media"

  private val chunkSize = 5 * 1024 * 1024 // 5 MB
  private val mediaReader = new MediaReader(chunkSize)

  /** Uploads media asynchronously from an absolute file path.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/media/upload-media/api-reference/post-media-upload" target="_blank">
    *   https://developer.twitter.com/en/docs/media/upload-media/api-reference/post-media-upload</a>.
    *
    * @param filePath : the absolute path of the file to upload.
    * @param additional_owners : By default is empty.
    *                          A comma-separated list of user IDs to set as additional owners
    *                          allowed to use the returned media_id in Tweets or Cards.
    *                          Up to 100 additional owners may be specified.
    * @return : The media details
    * */
  def uploadMediaFromPath(filePath: String, additional_owners: Seq[Long] = Seq.empty): Future[MediaDetails] = {
    uploadMediaFromFile(new File(filePath), additional_owners)
  }

  /** Uploads media asynchronously from a file.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/media/upload-media/api-reference/post-media-upload" target="_blank">
    *   https://developer.twitter.com/en/docs/media/upload-media/api-reference/post-media-upload</a>.
    *
    * @param file : the file to upload.
    * @param additional_owners : By default is empty.
    *                          A comma-separated list of user IDs to set as additional owners
    *                          allowed to use the returned media_id in Tweets or Cards.
    *                          Up to 100 additional owners may be specified.
    * @return : The media details
    * */
  def uploadMediaFromFile(file: File, additional_owners: Seq[Long] = Seq.empty): Future[MediaDetails] = {
    val size = file.length
    val inputStream = new FileInputStream(file)
    val filename = file.getName
    val mediaType = URLConnection.guessContentTypeFromName(filename)
    uploadMediaFromInputStream(inputStream, size, mediaType, Some(filename), additional_owners)
  }

  /** Uploads media asynchronously from an input stream.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/media/upload-media/api-reference/post-media-upload" target="_blank">
    *   https://developer.twitter.com/en/docs/media/upload-media/api-reference/post-media-upload</a>.
    *
    * @param inputStream : the input stream to upload.
    * @param size : the size of the data to upload.
    * @param media_type : the type of the media to upload.
    * @param filename : By default is `None`.
    *                 The filename used when uploading the media.
    * @param additional_owners : By default is empty.
    *                          A comma-separated list of user IDs to set as additional owners
    *                          allowed to use the returned media_id in Tweets or Cards.
    *                          Up to 100 additional owners may be specified.
    * @return : The media details
    * */
  def uploadMediaFromInputStream(inputStream: InputStream,
                                 size: Long,
                                 media_type: MediaType,
                                 filename: Option[String] = None,
                                 additional_owners: Seq[Long] = Seq.empty): Future[MediaDetails] =
    uploadMediaFromInputStream(inputStream, size, media_type.value, filename, additional_owners)

  private def uploadMediaFromInputStream(inputStream: InputStream,
                                         size: Long,
                                         media_type: String,
                                         filename: Option[String],
                                         additional_owners: Seq[Long]): Future[MediaDetails] = {
    def filenameBuilder(mediaId: Long) = {
      val extension = media_type.split("/", 2)
      filename.getOrElse(s"twitter4s-$mediaId.$extension")
    }

    implicit val ec: ExecutionContext = restClient.system.dispatcher

    for {
      details <- initMedia(size, media_type, additional_owners)
      uploads <- appendMedia(details.media_id, inputStream, filenameBuilder(details.media_id))
      finalize <- finalizeMedia(details.media_id)
    } yield finalize
  }

  private def initMedia(size: Long, media_type: String, additional_owners: Seq[Long]): Future[MediaDetails] = {
    import restClient._
    val parameters = MediaInitParameters(size, media_type, Some(additional_owners.mkString(",")))
    Post(s"$mediaUrl/upload.json", parameters).respondAs[MediaDetails]
  }

  private def appendMedia(mediaId: Long, inputStream: InputStream, filename: String)(
      implicit ec: ExecutionContext): Future[Seq[Unit]] = {
    val appendMediaById = appendMediaChunk(mediaId, filename) _
    Future.sequence(mediaReader.processAsChunks(inputStream, appendMediaById))
  }

  private def appendMediaChunk(mediaId: Long, filename: String)(chunk: Chunk, idx: Int)(
      implicit ec: ExecutionContext): Future[Unit] = {
    import restClient._
    val formData: FormData = FormData(
      Source(
        List(
          FormData.BodyPart.Strict("command", "APPEND"),
          FormData.BodyPart.Strict("media_id", mediaId.toString),
          FormData.BodyPart.Strict("segment_index", idx.toString),
          FormData.BodyPart.Strict("media_data", chunk.base64Data.mkString)
        )))
    Post(s"$mediaUrl/upload.json", formData).sendAsFormData
  }

  private def finalizeMedia(mediaId: Long): Future[MediaDetails] = {
    import restClient._
    val entity = MediaFinalizeParameters(mediaId)
    Post(s"$mediaUrl/upload.json", entity).respondAs[MediaDetails]
  }

  /** Returns the status of a media upload for pulling purposes.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/media/upload-media/api-reference/get-media-upload-status" target="_blank">
    *   https://developer.twitter.com/en/docs/media/upload-media/api-reference/get-media-upload-status</a>.
    *
    * @param media_id : The id of the media.
    * @return : The media details
    * */
  def statusMedia(media_id: Long): Future[MediaDetails] = {
    import restClient._
    val entity = MediaStatusParameters(media_id)
    Get(s"$mediaUrl/upload.json", entity).respondAs[MediaDetails]
  }

  /** This endpoint can be used to provide additional information about the uploaded media_id.
    * This feature is currently only supported for images and GIFs.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/media/upload-media/api-reference/post-media-metadata-create" target="_blank">
    *   https://developer.twitter.com/en/docs/media/upload-media/api-reference/post-media-metadata-create</a>.
    *
    * @param media_id : The id of the media.
    * @param description : The description of the media
    * */
  def createMediaDescription(media_id: Long, description: String): Future[Unit] = {
    import restClient._
    val entity = MediaMetadataCreation(media_id.toString, description)
    val jsonEntity = Serialization.write(entity)
    Post(s"$mediaUrl/metadata/create.json", jsonEntity, ContentTypes.`application/json`).sendAsFormData
  }
}

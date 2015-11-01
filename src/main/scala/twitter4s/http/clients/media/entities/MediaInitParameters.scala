package twitter4s.http.clients.media.entities

import twitter4s.http.marshalling.Parameters

case class MediaInitParameters(total_bytes: Long,
                     media_type: String, // TODO - enum!!
                     additional_owners: Option[String],
                     command: String = "INIT") extends Parameters

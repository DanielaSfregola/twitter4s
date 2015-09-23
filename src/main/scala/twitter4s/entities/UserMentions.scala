package twitter4s.entities

case class UserMentions(
                       screen_name: String,
                       name: String,
                       id: Long,
                       id_string: String,
                       indices: Seq[Long]
                       )

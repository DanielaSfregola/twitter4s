package twitter4s.entities

case class UserMentions(
                       name: String,
                       id: Long,
                       id_string: String,
                       indices: Seq[Int],
                       screen_name: String
                       )

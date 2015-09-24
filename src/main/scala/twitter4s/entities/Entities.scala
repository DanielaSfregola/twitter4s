package twitter4s.entities

case class Entities(
                   hashtags: Seq[HashTag],
                   media: Seq[Media],
                   urls: Seq[Url],
                   user_mentions: Option[UserMentions]
                   )

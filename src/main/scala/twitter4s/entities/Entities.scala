package twitter4s.entities

case class Entities(
                   hashtags: Seq[String], // TODO - it was empty
                   symbols: Seq[String], // TODO - it was empty
                   user_mentions: UserMentions, 
                   urls: Seq[String] // TODO - it was empty
                   )

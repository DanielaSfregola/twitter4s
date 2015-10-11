package twitter4s.entities

case class Suggestions(name: String,
                       slug: String,
                       size: Int,
                       users: Seq[User] = Seq.empty)

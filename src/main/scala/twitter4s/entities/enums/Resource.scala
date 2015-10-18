package twitter4s.entities.enums

object Resource extends Enumeration {
  type Resource = Value

  val Account = Value("account")
  val Application = Value("application")
  val Blocks = Value("blocks")
  val Collections = Value("collections")
  val Contacts = Value("contacts")
  val Device = Value("device")
  val DirectMessages = Value("direct_messages")
  val Favorites = Value("favorites")
  val Followers = Value("followers")
  val Friends = Value("friends")
  val Friendships = Value("friendships")
  val Geo = Value("geo")
  val Help = Value("help")
  val Lists = Value("lists")
  val Moments = Value("moments")
  val Mutes = Value("mutes")
  val SavedSearches = Value("saved_searches")
  val Search = Value("search")
  val Statuses = Value("statuses")
  val Trends = Value("trends")
  val Users = Value("users")

}

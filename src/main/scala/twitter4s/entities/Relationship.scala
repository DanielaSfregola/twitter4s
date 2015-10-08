package twitter4s.entities

case class Relationship(relationship: RelationshipOverview)

case class RelationshipOverview(source: RelationshipSource, target: RelationshipTarget)

case class RelationshipSource(id: Long,
                              id_str: String,
                              screen_name: String,
                              following: Boolean = false,
                              followed_by: Boolean = false,
                              following_received: Boolean = false,
                              following_requested: Boolean = false,
                              notifications_enabled: Boolean = false,
                              can_dm: Boolean = false,
                              blocking: Boolean = false,
                              blocked_by: Boolean = false,
                              muting: Boolean = false,
                              want_retweets: Boolean = false,
                              all_replies: Boolean = false,
                              marked_spam: Boolean =false)

case class RelationshipTarget(id: Long,
                              id_str: String,
                              screen_name: String,
                              following: Boolean = false,
                              followed_by: Boolean = false,
                              following_received: Boolean = false,
                              following_requested: Boolean = false)


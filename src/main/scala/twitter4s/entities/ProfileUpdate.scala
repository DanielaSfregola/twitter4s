package twitter4s.entities

case class ProfileUpdate(name: Option[String] = None,
                         url: Option[String] = None,
                         description: Option[String] = None,
                         location: Option[String] = None,
                         profile_link_color: Option[String] = None,
                         include_entities: Boolean = true,
                         skip_status: Boolean = false)

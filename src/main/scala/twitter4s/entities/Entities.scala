package twitter4s.entities

// TODO - conversion to clean this representation? see url vs urls
// unfortunately Twitter is not consistent when representing this entity...

case class Entities(hashtags: Seq[HashTag] = Seq.empty,
                    media: Seq[Media] = Seq.empty,
                    url: Option[Urls] = None,
                    urls: Seq[Url] = Seq.empty,
                    user_mentions: Seq[UserMention] = Seq.empty,
                    description: Option[Urls] = None)

case class Urls(urls: Seq[Url] = Seq.empty)




package com.danielasfregola.twitter4s.entities.v2

final case class TweetIncludes(tweets: Seq[Tweet],
                               users: Seq[User],
                               media: Seq[Media]
                               // places: Seq[Place], // TODO: Pending addition of places model
                               // polls: Seq[Polls] // TODO pending addition of polls model
                              )

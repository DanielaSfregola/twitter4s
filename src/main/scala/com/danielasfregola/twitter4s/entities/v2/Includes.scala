package com.danielasfregola.twitter4s.entities.v2

final case class Includes(tweets: Seq[Tweet]
                          // users: Seq[User], // TODO: Pending addition of users model
                          // places: Seq[Place], // TODO: Pending addition of places model
                          // media: Seq[Media], // TODO: Pending addition of media model
                          // polls: Seq[Polls] // TODO pending addition of polls model
                          )

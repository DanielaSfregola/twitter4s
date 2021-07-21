package com.danielasfregola.twitter4s.entities.v2

case class UserIncludes(tweets: Seq[Tweet],
                        users: Seq[User])

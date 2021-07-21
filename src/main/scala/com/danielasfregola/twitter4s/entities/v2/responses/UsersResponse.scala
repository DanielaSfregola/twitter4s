package com.danielasfregola.twitter4s.entities.v2.responses

import com.danielasfregola.twitter4s.entities.v2.{User, UserIncludes, Error}

case class UsersResponse(data: Seq[User],
                         includes: Option[UserIncludes],
                         errors: Seq[Error])

package com.danielasfregola.twitter4s.entities.v2.responses

import com.danielasfregola.twitter4s.entities.v2.{Error, UserIncludes, User}

case class UserResponse(data: Option[User],
                        includes: Option[UserIncludes],
                        errors: Seq[Error])

package com.danielasfregola.twitter4s.entities.v2.responses

import com.danielasfregola.twitter4s.entities.v2.{Error, User, UserIncludes}

case class UsersPaginatedResponse(
    data: Seq[User],
    includes: Option[UserIncludes],
    errors: Seq[Error],
    meta: ResponsePageMeta
)

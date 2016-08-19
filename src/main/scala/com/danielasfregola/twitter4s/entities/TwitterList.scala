package com.danielasfregola.twitter4s.entities

import java.util.Date

case class TwitterList(created_at: Date,
                       description: String,
                       following: Boolean,
                       full_name: String,
                       id: Long,
                       id_str: String,
                       name: String,
                       subscriber_count: Int,
                       uri: String,
                       member_count: Int,
                       mode: String,
                       slug: String,
                       user: User) extends EventTargetObject


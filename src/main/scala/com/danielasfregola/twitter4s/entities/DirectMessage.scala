package com.danielasfregola.twitter4s.entities

import java.util.Date

case class DirectMessage(created_at: Date,
                         entities: Option[Entities],
                         id: Long,
                         id_str: String,
                         recipient: User,
                         recipient_id: Long,
                         recipient_id_str: String,
                         recipient_screen_name: String,
                         sender: User,
                         sender_id: Long,
                         sender_id_str: String,
                         sender_screen_name: String,
                         text: String)








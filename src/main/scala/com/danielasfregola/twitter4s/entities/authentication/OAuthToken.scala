package com.danielasfregola.twitter4s.entities.authentication

case class OAuthToken(oauth_token: String, oauth_token_secret: String, oauth_callback_confirmed: Boolean)

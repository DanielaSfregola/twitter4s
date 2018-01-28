package com.danielasfregola.twitter4s.entities

final case class RateLimits(rate_limit_context: RateLimitContext, resources: Resources)

final case class RateLimitContext(access_token: String)

final case class Resources(lists: Map[String, RateLimit] = Map.empty,
                           application: Map[String, RateLimit] = Map.empty,
                           mutes: Map[String, RateLimit] = Map.empty,
                           friendships: Map[String, RateLimit] = Map.empty,
                           blocks: Map[String, RateLimit] = Map.empty,
                           geo: Map[String, RateLimit] = Map.empty,
                           users: Map[String, RateLimit] = Map.empty,
                           followers: Map[String, RateLimit] = Map.empty,
                           collections: Map[String, RateLimit] = Map.empty,
                           statuses: Map[String, RateLimit] = Map.empty,
                           contacts: Map[String, RateLimit] = Map.empty,
                           moments: Map[String, RateLimit] = Map.empty,
                           help: Map[String, RateLimit] = Map.empty,
                           friends: Map[String, RateLimit] = Map.empty,
                           direct_messages: Map[String, RateLimit] = Map.empty,
                           account: Map[String, RateLimit] = Map.empty,
                           favorites: Map[String, RateLimit] = Map.empty,
                           device: Map[String, RateLimit] = Map.empty,
                           saved_searches: Map[String, RateLimit] = Map.empty,
                           search: Map[String, RateLimit] = Map.empty,
                           trends: Map[String, RateLimit] = Map.empty)

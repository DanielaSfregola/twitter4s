package twitter4s.http.clients.subscriptions.parameters

import twitter4s.http.marshalling.Parameters

case class SubscriptionTimelineParameters(list_id: Option[Long] = None,
                                          slug: Option[String] = None,
                                          owner_screen_name: Option[String] = None,
                                          owner_id: Option[Long] = None,
                                          count: Int,
                                          since_id: Option[Long],
                                          max_id: Option[Long],
                                          include_entities: Boolean,
                                          include_rts: Boolean) extends Parameters

package com.danielasfregola.twitter4s.http.clients.rest.accountactivity

import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.http.clients.rest.RestClient
import com.danielasfregola.twitter4s.http.clients.rest.accountactivity.parameters.WebhooksParameters
import com.danielasfregola.twitter4s.util.Configurations._

import scala.concurrent.Future

/** Implements the available requests for the `account_activity` resource.
  * */
trait TwitterAccountActivityClient {

  protected val restClient: RestClient

  private val accountUrl = s"$apiTwitterUrl/$twitterVersion/account_activity/all"

  /** Registers a webhook URL for your environment.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#post-account-activity-all-env-name-webhooks" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#post-account-activity-all-env-name-webhooks</a>.
    *
    * @param env_name : The environment name is the name of the App that provide the access to the Account Activity API.
    * @param url : The encoded URL for the webhook callback endpoint.
    * @return : The webhook representation.
    * */
  def registerWebhook(env_name: String, url: String): Future[Webhook] = {
    import restClient._
    val parameters = WebhooksParameters(url)
    Post(s"$accountUrl/$env_name/webhooks.json", parameters).respondAs[Webhook]
  }

  /** Removes the webhook from the provided application.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#delete-account-activity-all-env-name-webhooks-webhook-id" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#delete-account-activity-all-env-name-webhooks-webhook-id</a>.
    *
    * @param env_name : The environment name is the name of the App that provide the access to the Account Activity API.
    * @param webhookId : The id of the webhook that you want to remove.
    * */
  def removeWebhook(env_name: String, webhookId: String): Future[Unit] = {
    import restClient._
    Delete(s"$accountUrl/$env_name/webhooks/$webhookId.json").sendAsFormData
  }

  /** Reenables the webhook by setting its status to valid. It triggers the challenge response check (CRC) for the given enviroments webhook for all activites.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#put-account-activity-all-env-name-webhooks-webhook-id" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#put-account-activity-all-env-name-webhooks-webhook-id</a>.
    *
    * @param env_name : The environment name is the name of the App that provide the access to the Account Activity API.
    * @param webhookId : The id of the webhook that you want to remove.
    * */
  def reenableWebhook(env_name: String, webhookId: String): Future[Unit] = {
    import restClient._
    Put(s"$accountUrl/$env_name/webhooks/$webhookId.json").sendAsFormData
  }

  /** Subscribes the provided application to all events for the provided environment for all message types.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#post-account-activity-all-env-name-subscriptions" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#post-account-activity-all-env-name-subscriptions</a>.
    *
    * @param env_name : The environment name is the name of the App that provide the access to the Account Activity API.
    * */
  def subscribeAll(env_name: String): Future[Unit] = {
    import restClient._
    Post(s"$accountUrl/$env_name/subscriptions.json").sendAsFormData
  }

  /** Deactivates subscription(s) for the provided user context and application for all activities.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#delete-account-activity-all-env-name-subscriptions-deprecated-" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#delete-account-activity-all-env-name-subscriptions-deprecated-</a>.
    *
    * @param env_name : The environment name is the name of the App that provide the access to the Account Activity API.
    * */
  def unsubscribeAll(env_name: String): Future[Unit] = {
    import restClient._
    Delete(s"$accountUrl/$env_name/subscriptions.json").sendAsFormData
  }

  /** Provides a way to determine if a webhook configuration is subscribed to the provided user’s events. If the provided user context has an active subscription with provided application, returns 204 OK. If the response code is not 204, then the user does not have an active subscription.
    * For more information see
    * <a href="https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#get-account-activity-all-env-name-subscriptions" target="_blank">
    *   https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#get-account-activity-all-env-name-subscriptions</a>.
    *
    * @param env_name : The environment name is the name of the App that provide the access to the Account Activity API.
    * */
  def isUserSubscribed(env_name: String): Future[Unit] = {
    import restClient._
    Get(s"$accountUrl/$env_name/subscriptions.json").sendAsFormData
  }

  // The following routes require a Bearer token to authenticate the corresponding requests
  // TODO - GET webhooks.json https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#get-account-activity-all-webhooks
  // TODO - GET subscriptions/count.json https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#get-account-activity-all-subscriptions-count
  // TODO - GET :env_name/subscriptions/list.json https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#get-account-activity-all-env-name-subscriptions-list
  // TODO - DELETE :env_name/subscriptions/:user_id.json (deprecate unsubscribeAll when implementing this endpoint) https://developer.twitter.com/en/docs/accounts-and-users/subscribe-account-activity/api-reference/aaa-premium#delete-account-activity-all-env-name-subscriptions-user-id-json
}

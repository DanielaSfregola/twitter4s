package com.danielasfregola.twitter4s.util

import org.specs2.mutable.Specification

class EncoderSpec extends Specification with Encoder {

  "HmacSha1 Encoder" should {

    "encode a base and secret as expected" in {
      val base = "POST&" +
        "https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&" +
        "include_entities%3Dtrue%26" +
        "oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26" +
        "oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26" +
        "oauth_signature_method%3DHMAC-SHA1%26" +
        "oauth_timestamp%3D1318622958%26" +
        "oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26" +
        "oauth_version%3D1.0%26" +
        "status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521"
      val secret = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE"
      toHmacSha1(base, secret) === "tnnArxj06cWHq44gCs1OSKk/jLY="
    }

    "encode a base and secret as expected with callback" in {
      val base = "POST&" +
        "https%3A%2F%2Fapi.twitter.com%2Foauth%2Frequest_token&" +
        "oauth_callback%3Dhttp%253A%252F%252Flocalhost%252Fsign-in-with-twitter%252F%26" +
        "oauth_consumer_key%3DcChZNFj6T5R0TigYB9yd1w%26" +
        "oauth_nonce%3Dea9ec8429b68d6b77cd5600adbbb0456%26" +
        "oauth_signature_method%3DHMAC-SHA1%26" +
        "oauth_timestamp%3D1318467427%26" +
        "oauth_version%3D1.0"
      val secret = "L8qq9PZyRg6ieKGEKhZolGC0vJWLw8iEJ88DRdyOg&"
      toHmacSha1(base, secret) === "F1Li3tvehgcraF8DMJ7OyxO4w9Y="
    }
  }
}

package com.danielasfregola.twitter4s.util

import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

trait HmacSha1Encoder {

  private val SHA1 = "HmacSHA1"
  private val UTF8 = "UTF-8"

  protected def toHmacSha1(base: String, secret: String): String = {
    val secretKeySpec = new SecretKeySpec(secret.getBytes(UTF8), SHA1)
    val mac = Mac.getInstance(SHA1)
    mac.init(secretKeySpec)
    val bytesToSign = base.getBytes(UTF8)
    val digest = mac.doFinal(bytesToSign)
    Base64.getEncoder.encodeToString(digest)
  }
}

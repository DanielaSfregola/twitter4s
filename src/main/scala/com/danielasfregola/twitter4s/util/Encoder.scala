package com.danielasfregola.twitter4s.util

import java.security.MessageDigest
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private[twitter4s] trait Encoder {

  def toHmacSha1(base: String, secret: String): String = {
    val HMAC_SHA1 = "HmacSHA1"
    val UTF8 = "UTF-8"
    val secretKeySpec = new SecretKeySpec(secret.getBytes(UTF8), HMAC_SHA1)
    val mac = Mac.getInstance(HMAC_SHA1)
    mac.init(secretKeySpec)
    val bytesToSign = base.getBytes(UTF8)
    val digest = mac.doFinal(bytesToSign)
    Base64.getEncoder.encodeToString(digest)
  }

  def toBase64(data: Array[Byte]): String =
    Base64.getEncoder.encodeToString(data)

  def toSha1(base: String): String = {
    val SHA1 = "SHA-1"
    val messageDigest = MessageDigest.getInstance(SHA1)
    val bytes = messageDigest.digest(base.getBytes)

    val stringBuffer = new StringBuffer
    bytes.foreach { byte =>
      stringBuffer.append(Integer.toString((byte & 0xff) + 0x100, 16).substring(1))
    }
    stringBuffer.toString
  }

}

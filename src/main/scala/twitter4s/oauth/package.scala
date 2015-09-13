package twitter4s

import java.net.URLEncoder

package object oauth {

  implicit class RichString(val value: String) {

    def toAscii = URLEncoder.encode(value, "UTF-8").replace("+", "%20")

  }
}

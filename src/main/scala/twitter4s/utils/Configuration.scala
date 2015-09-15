package twitter4s.utils

import com.typesafe.config.{ConfigFactory, Config}

trait Configuration {
  def config: Config = ConfigFactory.load()
}

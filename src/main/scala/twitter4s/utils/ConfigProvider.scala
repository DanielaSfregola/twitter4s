package twitter4s.utils

import com.typesafe.config.{ConfigFactory, Config}

trait ConfigProvider {
  def config: Config = ConfigFactory.load()
}

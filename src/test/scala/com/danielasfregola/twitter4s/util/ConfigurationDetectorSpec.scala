package com.danielasfregola.twitter4s.util

import com.typesafe.config.{Config, ConfigException}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class ConfigurationDetectorSpec extends Specification with Mockito {

  val myConfigFromEnvVar = "my-configuration-from-env-var"
  val myConfigFromFile = "my-configuration-from-file"

  abstract class ConfigurationDetectorSpecContext extends Scope {
    def config = mock[Config]

    val variableName = "MY-CONFIG"
    val configName = "my.config"
  }

  trait NoEnvVariable extends ConfigurationDetector {
    override protected def environmentVariable(name: String) = None
  }

  trait WithEnvVariable extends ConfigurationDetector {
    override protected def environmentVariable(name: String) = Some(myConfigFromEnvVar)
  }

  trait NoConfigFromFile extends ConfigurationDetector {
    override protected def configuration(path: String) = throw new ConfigException.Missing(path)
  }

  trait WithConfigFromFile extends ConfigurationDetector {
    override protected def configuration(path: String) = myConfigFromFile
  }

  "ConfigurationDetector" should {

    "if environment variable exists" in {

      "if configuration from file does not exists" in {
        "detect the configuration from the environment variable" in
          new ConfigurationDetectorSpecContext with WithEnvVariable with NoConfigFromFile {
            envVarOrConfig(variableName, configName) === Some(myConfigFromEnvVar)
          }
      }

      "if configuration from file exists" in {
        "detect the configuration from the environment variable" in
          new ConfigurationDetectorSpecContext with WithEnvVariable with WithConfigFromFile {
            envVarOrConfig(variableName, configName) === Some(myConfigFromEnvVar)
          }
      }
    }

    "if environment variable does not exist" in {

      "if configuration from file exists" in {
        "detect the configuration from the configuration file" in
          new ConfigurationDetectorSpecContext with NoEnvVariable with WithConfigFromFile {
            envVarOrConfig(variableName, configName) === Some(myConfigFromFile)
          }
      }

      "if configuration from file does not exist" in {
        "throw an exception" in
          new ConfigurationDetectorSpecContext with NoEnvVariable with NoConfigFromFile {
            envVarOrConfig(variableName, configName) === None
          }
      }
    }
  }
}

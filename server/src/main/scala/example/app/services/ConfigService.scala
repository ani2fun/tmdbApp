/*
 * Copyright Company
 */

package example.app.services

import com.typesafe.config.ConfigFactory

trait ConfigService {

  private val config = ConfigFactory.load()

  private val httpConfig = config.getConfig("http")

  val httpHost   = httpConfig.getString("host")
  val httpPort   = httpConfig.getInt("port")
  val serviceUri = httpConfig.getString("serviceUri")

  val tmdbAPI = config.getString("tmdbAPI")
  val apiKey  = config.getString("apiKey")

}

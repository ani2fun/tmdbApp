/*
 * Copyright Company
 */

package example.app.services

import com.typesafe.config.ConfigFactory

trait ConfigService {

  private val config = ConfigFactory.load()

  private val httpConfig = config.getConfig("http")

  val httpHost      = httpConfig.getString("interface")
  val httpPort      = httpConfig.getInt("port")
  val serviceHost   = httpConfig.getString("serviceHost")
  val serviceScheme = httpConfig.getString("serviceScheme")
  val serviceUri    = httpConfig.getString("serviceUri")

}

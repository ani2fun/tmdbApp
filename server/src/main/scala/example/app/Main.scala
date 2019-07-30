/*
 * Copyright Company
 */

package example.app

import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.http.scaladsl.settings.ServerSettings
import com.typesafe.config.ConfigFactory
import example.app.services.ConfigService
import net.codingwell.scalaguice.InjectorExtensions._
import org.slf4j.{ Logger, LoggerFactory }

object Main extends ConfigService {
  private val log: Logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]): Unit = {
    log.info("Main Method start")
    implicit val system: ActorSystem  = ActorSystem("main-system")
    implicit val ec: ExecutionContext = system.dispatcher

    val settings = ServerSettings(ConfigFactory.load).withVerboseErrorMessages(true)

    val injector = GuiceInjector.create

    val port: Int = sys.env.getOrElse("PORT", httpPort.toString).toInt

    injector
      .instance[WebServer]
      .startServer("0.0.0.0", port, settings, system)

    log.info("system terminate. exit main")
  }

}

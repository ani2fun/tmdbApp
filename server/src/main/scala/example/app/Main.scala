/*
 * Copyright Company
 */

package example.app

import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success }

import akka.actor.ActorSystem
import example.app.services.ConfigService
import net.codingwell.scalaguice.InjectorExtensions._
import org.slf4j.{ Logger, LoggerFactory }

object Main extends ConfigService {
  private val log: Logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]) {
    implicit val system: ActorSystem  = ActorSystem("main-system")
    implicit val ec: ExecutionContext = system.dispatcher

    val injector = GuiceInjector.create

    injector
      .instance[WebServer]
      .bind()
      .onComplete {
        case Success(binding) => log.info("HTTP WebServer Bound on {}", binding.localAddress)
        case Failure(error) => log.error("Failed", error)
        case _ => system.terminate()
      }

  }
}

/*
 * Copyright Company
 */

package example.app

import scala.concurrent.ExecutionContext

import akka.actor.{ ActorSystem, Scheduler }
import akka.stream.{ ActorMaterializer, Materializer }
import com.google.inject._
import com.typesafe.config.{ Config, ConfigFactory }
import example.app.services.ConfigService
import net.codingwell.scalaguice.{ ScalaModule, ScalaPrivateModule }
import org.slf4j.{ Logger, LoggerFactory }

import example.app.services.ConfigService

class GuiceModule(val config: Config) extends AbstractModule with ScalaModule with ConfigService {
  private val log: Logger = LoggerFactory.getLogger(getClass)

  override def configure(): Unit = {
    val system = ActorSystem("server-system")

    bind[Config].toInstance(config)
    bind[ActorSystem].annotatedWithName("server-system").toInstance(system)
    bind[ActorSystem].toInstance(ActorSystem())
    bind[ActorMaterializer].toInstance(ActorMaterializer()(system))
    bind[ExecutionContext].toInstance(system.dispatcher)
    bind[Scheduler].toInstance(system.scheduler)
    bind[Materializer].toInstance(ActorMaterializer()(system))
  }
}

class PrivateGuiceModule extends PrivateModule with ScalaPrivateModule {
  def configure(): Unit = {}
}

object GuiceInjector {
  lazy val create: Injector =
    Guice.createInjector(
      new GuiceModule(ConfigFactory.load()),
      new PrivateGuiceModule()
    )
}

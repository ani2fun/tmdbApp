/*
 * Copyright Company
 */

package example.app

import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ ExecutionContext, Future }

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import example.app.services.{ ApiService, ConfigService }

@Singleton
class WebServer @Inject()(
    val apiService: ApiService
)(implicit val system: ActorSystem, executor: ExecutionContext, materializer: ActorMaterializer)
    extends ConfigService {

  val port: Int = sys.env.getOrElse("PORT", httpPort.toString).toInt

  def bind(): Future[ServerBinding] =
    Http().bindAndHandle(
      apiService.routes,
      httpHost,
      port
    )
}

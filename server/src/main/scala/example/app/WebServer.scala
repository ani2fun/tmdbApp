/*
 * Copyright Company
 */

package example.app

import javax.inject.{ Inject, Singleton }

import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{ HttpApp, Route }
import akka.stream.ActorMaterializer
import example.app.services.ApiService

@Singleton
class WebServer @Inject()(
    val apiService: ApiService
)(implicit val system: ActorSystem, executor: ExecutionContext, materializer: ActorMaterializer)
    extends HttpApp {
  override protected def routes: Route = apiService.routes

  override protected def postHttpBinding(binding: Http.ServerBinding): Unit = {
    super.postHttpBinding(binding)
    val sys = systemReference.get()
    sys.log.info(s"Running on [${sys.name}] actor system")
  }

  override protected def postHttpBindingFailure(cause: Throwable): Unit = {
    val sys = systemReference.get()

    sys.log.debug(s"The server could not be started due to $cause")
  }
}

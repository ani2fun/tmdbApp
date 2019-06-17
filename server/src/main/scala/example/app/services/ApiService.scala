/*
 * Copyright Company
 */

package example.app.services

import javax.inject.Inject

import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import example.app.controllers.{ BaseController, _ }

class ApiService @Inject()(
    baseController: BaseController,
    movieController: MovieController
)(implicit executor: ExecutionContext, as: ActorSystem, mat: Materializer) {

  def routes: server.Route = baseController.routes ~ movieController.routes
}

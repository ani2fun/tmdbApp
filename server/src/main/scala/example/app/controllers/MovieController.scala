/*
 * Copyright Company
 */

package example.app.controllers

import javax.inject.{ Inject, Singleton }

import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success }

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{ Directives, Route }
import example.app.services.{ ConfigService, MovieService }
@Singleton
// scalastyle:off multiple.string.literals
class MovieController @Inject()(implicit val ec: ExecutionContext, val movieService: MovieService)
    extends ConfigService
    with Directives {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

  val routes: Route = getMovie

  def getMovie: Route = path("findmovie") {
    get {
      parameters('movieName.as[String]) { movieName =>
        onComplete(movieService.findMovie(movieName)) {
          case Success(res) => complete(StatusCodes.Accepted -> s"Movie found! ---> $res")
          case Failure(ex) => complete(StatusCodes.InternalServerError -> s"An error occurred: ${ex.getMessage}")
        }
      }
    }
  }
}

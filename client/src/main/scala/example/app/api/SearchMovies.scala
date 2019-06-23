/*
 * Copyright Company
 */

package example.app.api

import scala.concurrent.{ ExecutionContext, Future }

import example.app.shared.Movie
import example.app.utils.api.Api
import japgolly.scalajs.react.Callback

class SearchMovies(val baseUrl: String) extends Api {

  //https://image.tmdb.org/t/p/w185/hEpWvX6Bp79eLxY1kX5ZZJcme5U.jpg

  def getMovie(movieName: String)(implicit ec: ExecutionContext): Future[Option[Seq[Movie]]] = {
    Callback.log(s"movieName ---->  $movieName")
    futureGet[Option[Seq[Movie]]](s"findmovie?moviename=$movieName")
  }

  def getTrendingMovies(implicit ec: ExecutionContext): Future[Option[Seq[Movie]]] = {
    Callback.log(s"trending movies ----> ")
    futureGet[Option[Seq[Movie]]](s"trending")
  }
}

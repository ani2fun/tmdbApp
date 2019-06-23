/*
 * Copyright Company
 */

package example.app.api

import scala.concurrent.{ ExecutionContext, Future }

import example.app.shared.Movie
import example.app.utils.api.Api

// scalastyle:off multiple.string.literals
class SearchMovies(val baseUrl: String) extends Api {

  def getMovie(movieName: String)(implicit ec: ExecutionContext): Future[Option[Seq[Movie]]] =
    futureGet[Option[Seq[Movie]]](s"findmovie?moviename=$movieName")

  def getTrendingMovies(implicit ec: ExecutionContext): Future[Option[Seq[Movie]]] =
    futureGet[Option[Seq[Movie]]](s"trending")
}

/*
 * Copyright Company
 */

package example.app.api

import scala.concurrent.{ ExecutionContext, Future }
import example.app.shared.Movie
import example.app.utils.api.Api

class SearchMovies(val baseUrl: String) extends Api {

  // https://api.themoviedb.org/3/search/movie?api_key=5fdcb6eafc5f7c6b238952774693c9a9&include_adult=false&page=1&language=en-US&query=the+avengers

  private val apiKey = s"5fdcb6eafc5f7c6b238952774693c9a9"

  def getMovie(movieName: String)(implicit ec: ExecutionContext): Future[Movie] = {

    val queryString = Seq(
      s"api_key=$apiKey",
      s"include_adult=false",
      s"language=en-US",
      s"query=$movieName"
    ).mkString("&")

//    futureGet[Movie](s"search/movie?$queryString")
    futureGet[Movie](s"findmovie?$movieName")

//    println(s"moviename passed $movieName")
//    Future.successful(Movie(1, Some(movieName), false))

  }
}

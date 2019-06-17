/*
 * Copyright Company
 */

package example.app.model

import scala.language.postfixOps

import example.app.shared.Movie

object MoviesState {

  val movieName           = ""
  val emptyMovie          = Movie(1, Some(""), false)
  val emptyTrendingMovies = Seq()

}

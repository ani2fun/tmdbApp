/*
 * Copyright Company
 */

package example.app.model

import scala.language.postfixOps

import example.app.shared.Movie

object MoviesState {

  val movieName = ""
  val emptyMovie = Movie(
    Some(1726),
    Some("Iron Man"),
    Some(
      "After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil."
    ),
    Some("2008-04-30"),
    Some("/jdXmMoREwmfZWxtqGyk5Zv0UB6r.jpg"),
    Some("/ZQixhAZx6fH1VNafFXsqa1B8QI.jpg")
  )
  val emptyTrendingMovies = Seq()

}

/*
 * Copyright Company
 */

package example.app.model

import scala.language.postfixOps

import example.app.shared.Movie

// scalastyle:off magic.number
object MoviesState {

  val movieName = ""
  val defaultMovie = Movie(
    Some(320288),
    Some("Dark Phoenix"),
    Some(
      """
        |The X-Men face their most formidable and powerful foe when one of their own, Jean Grey, starts to spiral out of control.
        |During a rescue mission in outer space, Jean is nearly killed when she's hit by a mysterious cosmic force.
        |Once she returns home, this force not only makes her infinitely more powerful, but far more unstable.
        |The X-Men must now band together to save her soul and battle aliens that want to use Grey's new abilities to rule the galaxy.
      """.stripMargin
    ),
    Some("2008-04-30"),
    Some("/kZv92eTc0Gg3mKxqjjDAM73z9cy.jpg"),
    Some("/phxiKFDvPeQj4AbkvJLmuZEieDU.jpg")
  )
  val emptyTrendingMovies = Seq()

}

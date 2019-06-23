/*
 * Copyright Company
 */

package example.app.components

import example.app.shared.Movie
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._

object Card {

  case class Props(movie: Movie)

  class Backend($ : BackendScope[Props, Unit]) {

    def render(props: Props): VdomElement =
      <.div(
        ^.cls := "card",
        <.div(
          ^.cls := "row no-gutters",
          <.div(
            ^.cls := "col-auto",
            <.figure(<.img(^.src := s"https://image.tmdb.org/t/p/w154${props.movie.poster_path.getOrElse(None)}"))
          ),
          <.div(
            ^.cls := "col",
            <.div(
              ^.cls := "card-block px-2",
              <.h3(
                ^.cls := "card-title",
                s"${props.movie.original_title.getOrElse(None)}"
              ),
              <.p(
                ^.cls := "card-text",
                s"${props.movie.overview.getOrElse(None)}"
              ),
              <.span(
                <.i(
                  <.b("Release Date: "),
                  s"${props.movie.release_date.getOrElse(None)}"
                )
              )
            )
          )
        )
      )
  }

  val component =
    ScalaComponent
      .builder[Props]("Card")
      .renderBackend[Backend]
      .build

  def apply(movie: Movie): Unmounted[Props, Unit, Backend] = component(Props(movie))
}

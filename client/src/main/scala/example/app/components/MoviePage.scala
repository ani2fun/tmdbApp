/*
 * Copyright Company
 */

package example.app.components

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{ Failure, Success }

import japgolly.scalajs.react.{ Callback, _ }
import example.app.api.TrendingMovies
import example.app.model.MoviesState
import example.app.shared.Movie
import example.app.utils.api.ApiClient
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._

// scalastyle:off multiple.string.literals
object MoviePage {

  case class Props(apiClient: ApiClient)

  case class State(movieName: String, movie: Movie, movieListingState: Seq[Movie])

  class Backend($ : BackendScope[Props, State]) {

    // For searching movies

    def movieNameState(name: String): CallbackTo[Unit]         = $.modState(_.copy(movieName = name))
    def updateMovieNameInput(e: ReactEventFromInput): Callback = movieNameState(e.target.value)

    def updateMovieState(movie: Movie): CallbackTo[Unit] = $.modState(_.copy(movie = movie))

    def searchMovie(name: String, apiClient: ApiClient) = Callback.future {
      for {
        movie <- apiClient.searchMoviesClient.getMovie(name)
      } yield {
        $.modState(_.copy(movie = movie))
      }
    }

    def onTextChange(e: ReactEventFromInput): Callback =
      Callback.info("Value received = " + e.target.value)

    def render(props: Props, state: State): VdomElement =
      <.div(
        <.nav(
          ^.cls := "navbar navbar-light bg-light",
          <.a(
            ^.cls := "navbar-brand",
            ^.href := "#",
            <.img(
              ^.cls := "d-inline-block align-top",
              ^.width := 30.px,
              ^.height := 30.px,
              ^.src := "assets/img/movie.svg"
            ),
            "   TMDb  "
          )
        ),
        <.div(
          ^.cls := "container",
          <.h4("Search Movie"),
          <.div(
            ^.cls := "form-group",
            <.div(
              ^.cls := "row",
              ^.id := "movie",
              <.form(
                ^.cls := "form-inline",
                <.input.text(
                  ^.cls := "form-control mr-sm-2",
                  ^.`type` := "text",
                  ^.placeholder := "Search",
                  ^.aria.labelledBy := "Search",
                  ^.value := state.movieName,
                  ^.onChange ==> updateMovieNameInput
                ),
                <.button(
                  ^.cls := "btn btn-outline-success my-2 my-sm-0",
                  ^.`type` := "submit",
                  ^.onClick --> searchMovie(state.movieName, props.apiClient),
                  "Search"
                )
              )
            ),
            <.div(
              ^.cls := "row",
              <.div(<.span(state.movie.originalTitle))
            )
          )
        )
      )
  }

  val component =
    ScalaComponent
      .builder[Props]("MoviePage")
      .initialState(
        State(
          MoviesState.movieName,
          MoviesState.emptyMovie,
          MoviesState.emptyTrendingMovies
        )
      )
      .renderBackend[Backend]
      .build

  def apply(apiClient: ApiClient): Unmounted[Props, State, Backend] = component(Props(apiClient))
}

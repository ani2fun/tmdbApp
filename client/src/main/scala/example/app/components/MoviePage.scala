/*
 * Copyright Company
 */

package example.app.components

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

import example.app.model.MoviesState
import example.app.shared.Movie
import example.app.utils.api.ApiClient
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ Callback, _ }

// scalastyle:off multiple.string.literals
object MoviePage {
  //https://image.tmdb.org/t/p/w185/hEpWvX6Bp79eLxY1kX5ZZJcme5U.jpg
  case class Props(movie: Movie, apiClient: ApiClient)

  case class State(movieName: String, movie: Movie, tendingMovies: Seq[Movie])

  class Backend($ : BackendScope[Props, State]) {

    // State updates
    def movieNameState(name: String): CallbackTo[Unit] = $.modState(_.copy(movieName = name))

    def updateMovieNameInput(e: ReactEventFromInput): Callback = movieNameState(e.target.value)

    def updateMovieState(movie: Movie): CallbackTo[Unit] = $.modState(_.copy(movie = movie))

    def updateTrendingMoviesState(movies: Seq[Movie]): CallbackTo[Unit] = $.modState(_.copy(tendingMovies = movies))

    // API calls
    def searchMovie(movieName: String, apiClient: ApiClient)(e: ReactEventFromInput): Callback = Callback.future {
      e.preventDefault()
      for {
        movie <- apiClient.searchMoviesClient.getMovie(movieName)
      } yield {
        val takeFirst = movie.map(_.head).get
        Callback.log(s"takeFirst : $takeFirst")
        updateMovieState(takeFirst)
      }
    }

    def trendingMovies(apiClient: ApiClient)(e: ReactEventFromInput): Callback = Callback.future {
      e.preventDefault()
      for {
        trending <- apiClient.searchMoviesClient.getTrendingMovies
      } yield {
        updateTrendingMoviesState(trending.head)
      }
    }

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
                  ^.onClick ==> searchMovie(state.movieName, props.apiClient),
                  "Search"
                )
              )
            ),
            <.br,
            <.div(
              ^.cls := "row",
              <.div(
                ^.cls := "card",
                <.div(
                  ^.cls := "row no-gutters",
                  <.div(
                    ^.cls := "col-auto",
                    <.img(^.src := s"https://image.tmdb.org/t/p/w154${state.movie.poster_path.getOrElse(None)}")
                  ),
                  <.div(
                    ^.cls := "col",
                    <.div(
                      ^.cls := "card-block px-2",
                      <.h3(
                        ^.cls := "card-title",
                        s"${state.movie.original_title.getOrElse(None)}"
                      ),
                      <.p(
                        ^.cls := "card-text",
                        s"${state.movie.overview.getOrElse(None)}"
                      ),
                      <.span(<.i(s"Release Date: ${state.movie.release_date.getOrElse(None)}"))
                    )
                  )
                )
              )
            )
          ),
          <.hr,
          <.div(
            <.h4("Trending Movies"),
            <.button(
              ^.cls := "btn btn-outline-success my-2 my-sm-0",
              ^.`type` := "submit",
              ^.onClick ==> trendingMovies(props.apiClient),
              "Get Trending Movies"
            )
          ),
          <.div(<.p(s"${state.tendingMovies.map { _.original_title.getOrElse("") }}")),
          <.br
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

  def apply(apiClient: ApiClient): Unmounted[Props, State, Backend] = component(Props(MoviesState.emptyMovie, apiClient))
}

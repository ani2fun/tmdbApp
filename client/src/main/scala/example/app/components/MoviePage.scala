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

  case class Props(apiClient: ApiClient)

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
      Callback.info("movieName to search " + movieName)

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
            <.div(
              ^.cls := "row",
              <.div(
                <.span(state.movie.id),
                <.hr,
                <.span(state.movie.original_title),
                <.hr,
                <.span(state.movie.overview),
                <.hr,
                <.span(state.movie.release_date),
                <.hr,
                <.span(state.movie.poster_path)
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
          )
          /*,
          <.div(state.tendingMovies.head.originalTitle.get)
         */
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

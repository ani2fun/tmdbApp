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
object Page {

  case class Props(movie: Movie, tendingMovies: Seq[Movie], apiClient: ApiClient)

  case class State(name: String, movie: Movie, trendingMovies: Seq[Movie])

  class Backend($ : BackendScope[Props, State]) {
    // State updates

    def updateTrendingMoviesState(movies: Seq[Movie]): CallbackTo[Unit] = $.modState(_.copy(trendingMovies = movies))

    // API calls
    def searchMovie(name: String, apiClient: ApiClient): Callback = Callback.future {
      for {
        movie <- apiClient.searchMoviesClient.getMovie(name)
      } yield {
        val takeFirst = movie.map(_.head).get
        Callback.log(s"takeFirst : $takeFirst")
        $.modState(_.copy(movie = takeFirst))
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

    def removeFromTrending(movie: Movie): Callback =
      $.modState(state => state.copy(trendingMovies = state.trendingMovies.filterNot(_.id.contains(movie.id.get))))

    def render(props: Props, state: State): VdomElement = {
      Callback.log("render")
      <.div(
        Header(name => searchMovie(name, props.apiClient)),
        <.div(
          ^.cls := "container",
          <.h4("Search Movie"),
          <.div(
            <.br,
            <.div(
              ^.cls := "row",
              <.div(Card(state.movie))
            )
          ),
          <.hr,
          <.div(
            <.h4("Trending Movies"),
            <.button(
              ^.cls := "btn btn-outline-success my-2 my-sm-0",
              ^.`type` := "submit",
              ^.onClick ==> trendingMovies(props.apiClient),
              "Show"
            )
          ),
          <.br,
          <.div(
            state.trendingMovies.toTagMod { movie =>
              <.div(
                ^.cls := "row",
                <.i(
                  ^.cls := "fas fa-times-circle",
                  ^.onClick --> removeFromTrending(movie)
                ),
                Card(movie),
                <.br
              )
            }
          )
        )
      )
    }
  }

  val component =
    ScalaComponent
      .builder[Props]("MoviePage")
      .initialState(
        State(
          MoviesState.movieName,
          MoviesState.defaultMovie,
          MoviesState.emptyTrendingMovies
        )
      )
      .renderBackend[Backend]
      .shouldComponentUpdate(_ => CallbackTo(true))
      .build

  def apply(apiClient: ApiClient): Unmounted[Props, State, Backend] =
    component(
      Props(
        MoviesState.defaultMovie,
        MoviesState.emptyTrendingMovies,
        apiClient
      )
    )
}

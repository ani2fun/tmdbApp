/*
 * Copyright Company
 */

package example.app.components

import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ BackendScope, Callback, ScalaComponent, _ }
import org.scalajs.dom.html.Element

// scalastyle:off multiple.string.literals
object Header {

  case class State(name: String)

  case class Props(search: String => Callback)

  class Backend($ : BackendScope[Props, State]) {
    // State updates
    def movieNameState(name: String): CallbackTo[Unit] = $.modState(_.copy(name = name))

    def updateMovieNameInput(event: ReactEventFromInput): Callback = movieNameState(event.target.value)

    def render(props: Props, state: State): VdomTagOf[Element] =
      <.nav(
        ^.cls := "navbar navbar-light bg-light",
        <.div(
          ^.cls := "container",
          <.div(
            ^.cls := "row",
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
            ),
            <.form(
              ^.cls := "form-inline",
              <.input.text(
                ^.cls := "form-control mr-sm-2",
                ^.`type` := "text",
                ^.placeholder := "Search",
                ^.aria.labelledBy := "Search",
                ^.value := state.name,
                ^.onChange ==> updateMovieNameInput
              ),
              <.button(
                ^.cls := "btn btn-outline-success my-2 my-sm-0",
                ^.`type` := "submit",
                ^.onClick ==> (_.preventDefaultCB >> props.search(state.name)),
                "Search"
              )
            )
          )
        )
      )
  }

  val component =
    ScalaComponent
      .builder[Props](displayName = "Trending")
      .initialState(State(""))
      .renderBackend[Backend]
      .build

  def apply(search: String => Callback): VdomElement =
    component(Props(search))
}

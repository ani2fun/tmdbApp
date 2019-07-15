/*
 * Copyright Company
 */

package example.app.controllers

import javax.inject.{ Inject, Singleton }

import scala.concurrent.ExecutionContext

import akka.http.scaladsl.server.Directives
import example.app.services.ConfigService
import example.app.twirl.Implicits._

// scalastyle:off multiple.string.literals
@Singleton
class BaseController @Inject()(implicit val ec: ExecutionContext) extends ConfigService with Directives {

  val routes = pathEndOrSingleSlash {
    get {
      complete {
        example.app.html.index.render(serviceUri)
      }
    }
  } ~ pathPrefix("assets" / Remaining) { file =>
    encodeResponse {
      getFromResource("public/" + file)
    }
  } ~ path("main.ico") {
    get {
      encodeResponse {
        getFromResource("public/img/movie.ico")
      }
    }
  } ~ pathEndOrSingleSlash {
    get {
      getFromResource("public/img/favicon.ico")
    }
  }

}

/*
 * Copyright Company
 */

package example.app.api

import example.app.shared.Movie
import example.app.utils.api.{ Api, ApiMethods, ApiStatusCodes }
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.extra.Ajax

class TrendingMovies(val baseUrl: String) extends Api {

  import io.circe.parser._

  def getTrendingMovies(success: Seq[Movie] => Callback, error: (Int, String) => Callback = error): Callback =
    Ajax(
      ApiMethods.GET,
      s"$baseUrl/trending"
    ).setRequestContentTypeJsonUtf8.send.onComplete { res =>
      res.status match {
        case ApiStatusCodes.OK =>
          decode[Seq[Movie]](res.responseText) match {
            case Right(movies) => success(movies)
            case Left(e) => error(ApiStatusCodes.INTERNAL_ERROR, e.getMessage)
          }
        case e: Int => error(e, res.responseText)
      }
    }.asCallback

}

/*
 * Copyright Company
 */

package example.app.utils.api

import scala.concurrent.{ ExecutionContext, Future }

import io.circe.Decoder
import io.circe.parser._
import japgolly.scalajs.react.Callback
import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.{ Ajax => FutureAjax }

trait Api {
  val MaxPaginationSize = 10000

  protected val ContentType           = "Content-Type"
  protected val ApplicationJson       = "application/json"
  protected val JsonContentTypeHeader = Map(ContentType -> ApplicationJson)

  protected val baseUrl: String

  protected def error(errorCode: Int, errorMessage: String) = Callback { (errorCode, errorMessage) }

  def futureGet[Response](url: String, successCode: Int = ApiStatusCodes.OK)(
      implicit decoder: Decoder[Response],
      ec: ExecutionContext
  ): Future[Response] =
    FutureAjax
      .get(
        s"$baseUrl/$url",
        headers = JsonContentTypeHeader
      )
      .flatMap(onCompleteFuture(successCode))

  protected def onCompleteFuture[Response](
      successCode: Int
  )(implicit decoder: Decoder[Response]): XMLHttpRequest => Future[Response] = res => {
    res.status match {
      case `successCode` =>
        Future.fromTry(decode[Response](res.responseText).toTry)
      case e: Int => Future.failed(new Exception(s"An Error occured: ${res.responseText}"))
    }
  }
}

object Api {
  type ApiError = (Int, String)
}

object ApiMethods {
  val GET    = "GET"
  val POST   = "POST"
  val PUT    = "PUT"
  val DELETE = "DELETE"
}

object ApiStatusCodes {
  val OK             = 200
  val CREATED        = 201
  val ACCEPTED       = 202
  val NOT_FOUND      = 404
  val CONFLICT       = 409
  val UNAUTHORIZED   = 401
  val INTERNAL_ERROR = 500
}

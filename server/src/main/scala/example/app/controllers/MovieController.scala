/*
 * Copyright Company
 */

package example.app.controllers

import javax.inject.{ Inject, Singleton }

import scala.collection.immutable
import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success }

import akka.http.scaladsl.marshalling.{ Marshal, ToEntityMarshaller }
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.server.{ Directives, Route }
import example.app.services.{ ConfigService, MovieService }
import io.circe.syntax._
import io.circe.{ Encoder, Json }

@Singleton
// scalastyle:off multiple.string.literals
class MovieController @Inject()(implicit val ec: ExecutionContext, val movieService: MovieService)
    extends ConfigService
    with Directives {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

  val routes: Route = getMovie ~ getTrendingMovies

  def getMovie: Route = path("findmovie") {
    get {
      parameters('moviename.as[String]) { movieName =>
        onComplete(movieService.searchMovieByName(movieName)) {
          case Success(res) =>
            complete(
              HttpResponse(
                StatusCodes.OK,
                List(`Content-Type`(`application/json`)),
                entity = res.asJson.noSpaces
              )
            )
          case Failure(ex) => complete(StatusCodes.InternalServerError -> s"An error occurred: ${ex.getMessage}")
        }
      }
    }
  }

  def getTrendingMovies: Route = path("trending") {
    get {
      parameters('mediatype.as[String].?, 'timewindow.as[String].?) { (mediatype, timewindow) =>
        onComplete(movieService.getTrendingMovies(mediatype, timewindow)) {
          case Success(res) =>
            complete(
              HttpResponse(
                StatusCodes.OK,
                List(`Content-Type`(`application/json`)),
                entity = res.asJson.noSpaces
              )
            )
          case Failure(ex) => complete(StatusCodes.InternalServerError -> s"An error occurred: ${ex.getMessage}")
        }
      }
    }
  }

  def send(statusCode: StatusCode): Route = complete(statusCode)

  def send[T](statusCode: StatusCode, content: T, headers: immutable.Seq[HttpHeader] = Nil)(
      implicit marshaller: ToEntityMarshaller[T],
      ec: ExecutionContext
  ): Route = {
    val response = Marshal(content)
      .to[ResponseEntity](marshaller, ec)
      .map(entity => {
        HttpResponse(statusCode, headers = headers).withEntity(entity)
      })
    complete(response)
  }

  def sendJson[T](statusCode: StatusCode, content: T)(implicit encoder: Encoder[T], ec: ExecutionContext): Route =
    sendJson(statusCode, content.asJson)

  def sendJson[T](content: T)(implicit encoder: Encoder[T], ec: ExecutionContext): Route =
    sendJson(StatusCodes.OK, content)

  def sendJson(statusCode: StatusCode, json: Json)(implicit ec: ExecutionContext): Route =
    send(statusCode, Option(json.noSpaces))

}

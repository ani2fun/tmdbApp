/*
 * Copyright Company
 */

package example.app.services

import javax.inject.Inject

import scala.concurrent.{ ExecutionContext, Future }

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import example.app.shared.{ Movie, TmdbResponse }
import io.circe.Decoder.Result
import io.circe.ParsingFailure
import io.circe.parser._
import org.slf4j.{ Logger, LoggerFactory }

class MovieService @Inject()(implicit ex: ExecutionContext) extends ConfigService {

  private val log: Logger   = LoggerFactory.getLogger(getClass)
  implicit val system       = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val http = Http(system)

  def searchMovieByName(name: String): Future[Option[Seq[Movie]]] =
    for {
      httpResponse <- searchMovieByNameHttpReq(name)
      entity <- Unmarshal(httpResponse.entity).to[String].map(_.stripMargin)
    } yield {
      // Parse json to TmdbResponse
      val parsed: Either[ParsingFailure, Result[TmdbResponse]] =
        parse(entity).map(_.as[TmdbResponse])

      // Get Decoder result
      val decoderResult: Option[TmdbResponse] = parsed.toOption.flatMap(_.toOption)

      /* Return Single movie from Seq[Movie] from the TmdbResponse results
       * As the closest matching movie of your search return by TMDB API
       */
      decoderResult.map(_.results.head)
    }

  def searchMovieByNameHttpReq(movieName: String): Future[HttpResponse] = {
    val queryString = Seq(
      s"api_key=$apiKey",
      s"include_adult=false",
      s"language=en-US",
      s"query=${movieName.replaceAll("\\s", "+").mkString}"
    ).mkString("&")

    val akkaUri = Uri(s"${tmdbAPI}search/movie?").withQuery(Uri.Query(queryString))

    http.singleRequest(HttpRequest(uri = akkaUri))
  }

  def getTrendingMovies(mediaType: Option[String], timeWindow: Option[String]): Future[Option[Seq[Movie]]] =
    for {
      httpResponse <- getTrendingMoviesHttpReq(
        mediaType.getOrElse("movie"),
        timeWindow.getOrElse("week")
      )
      entity <- Unmarshal(httpResponse.entity).to[String].map(_.stripMargin)
    } yield {
      // Parse json to TmdbResponse
      val parsed: Either[ParsingFailure, Result[TmdbResponse]] =
        parse(entity).map(_.as[TmdbResponse](TmdbResponse.decoderTmdbResponse))

      // Get Decoder result
      val decoderResult: Option[TmdbResponse] = parsed.toOption.flatMap(_.toOption)

      // Return Trending movies from the TmdbResponse results
      decoderResult.map(_.results.head)
    }

  def getTrendingMoviesHttpReq(mediaType: String, timeWindow: String): Future[HttpResponse] = {

    val akkaUri = Uri(s"${tmdbAPI}trending/$mediaType/$timeWindow?api_key=$apiKey")

    http.singleRequest(HttpRequest(uri = akkaUri))
  }

}

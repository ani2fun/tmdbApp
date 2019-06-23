/*
 * Copyright Company
 */

package example.app.shared

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{Decoder, Encoder}

case class TmdbResponse(page: Option[Int], total_results: Option[Int], total_pages: Option[Int], results: Option[Seq[Movie]])

object TmdbResponse {

  implicit val encoderTmdbResponse: Encoder[TmdbResponse] = deriveEncoder[TmdbResponse]
  implicit val decoderTmdbResponse: Decoder[TmdbResponse] = deriveDecoder[TmdbResponse]

}

case class Movie(id: Option[Int],
                 original_title: Option[String],
                 overview: Option[String],
                 release_date: Option[String],
                 poster_path: Option[String],
                 backdrop_path: Option[String])

object Movie {

  type OptStr = Option[String]
  implicit val encoderMovie: Encoder[Movie] = deriveEncoder[Movie]
  implicit val decoderMovie: Decoder[Movie] = deriveDecoder[Movie]

}

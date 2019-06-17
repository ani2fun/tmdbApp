/*
 * Copyright Company
 */

package example.app.shared

import io.circe.{Decoder, Encoder}
import io.circe.generic.decoding.DerivedDecoder
import io.circe.generic.encoding.DerivedObjectEncoder

case class Movie(id: Int,
                 originalTitle: Option[String],
//                 title: Option[String],
//                 backdropPath: Option[String],
//                 posterPath: Option[String],
//                 overview: Option[String],
//                 releaseDate: Option[String],
//                 averageVote: Option[Double],
//                 voteCount: Option[Int],
//                 popularity: Option[Double],
                 toDelete: Boolean = false)


object Movie {
  implicit val encoder: Encoder[Movie] = DerivedObjectEncoder.deriveEncoder
  implicit val decoder: Decoder[Movie] = DerivedDecoder.deriveDecoder
}

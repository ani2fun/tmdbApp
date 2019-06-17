/*
 * Copyright Company
 */

package example.app.services

import javax.inject.Inject

import scala.concurrent.{ ExecutionContext, Future }

import example.app.shared.Movie

class MovieService @Inject()(implicit executionContext: ExecutionContext) {

  def findMovie(name: String): Future[Option[Movie]] = Future.successful(Some(Movie(1, Some(name), false)))

}

/*
 * Copyright Company
 */

package example.app.utils.api

import example.app.api.{ SearchMovies, TrendingMovies }

class ApiClient(val baseUrl: String) {
  val trendingMoviesClient = new TrendingMovies(baseUrl)
  val searchMoviesClient   = new SearchMovies(baseUrl)
}

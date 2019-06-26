/*
 * Copyright Company
 */

package example.app

import scala.scalajs.js.annotation.JSExport

import example.app.components.Page
import example.app.utils.api.ApiClient
import japgolly.scalajs.react.extra.router.BaseUrl
import org.scalajs.dom

object ReactApp {

  @JSExport
  def main(args: Array[String]): Unit = {

    // Parameters rendered from Server
    val appContent = dom.document.getElementById("app-content")
    val serviceUri = appContent.getAttribute("data-serviceUri")

    val serviceUrl =
      if (serviceUri.nonEmpty) {
        BaseUrl.fromWindowOrigin / (if (serviceUri.endsWith("/")) serviceUri.substring(0, serviceUri.length - 1) else serviceUri)
      } else {
        BaseUrl.fromWindowOrigin
      }

    val apiClient = new ApiClient(serviceUrl.value)

    Page(apiClient).renderIntoDOM(appContent)

  }
}

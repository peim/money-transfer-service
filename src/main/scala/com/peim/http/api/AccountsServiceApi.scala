package com.peim.http.api

import akka.http.scaladsl.server.Directives.{complete, get, pathEndOrSingleSlash, pathPrefix}
import akka.http.scaladsl.server.Route

class AccountsServiceApi {

  val route: Route = pathPrefix("accounts") {
    pathEndOrSingleSlash {
      get {
        complete(getAccounts)
      }
    }
  }

  private def getAccounts = ???
}

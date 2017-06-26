package com.peim.http.api

import akka.http.scaladsl.server.Directives.{complete, get, pathEndOrSingleSlash, pathPrefix}
import akka.http.scaladsl.server.Route

class UsersServiceApi {

  val route: Route = pathPrefix("users") {
    pathEndOrSingleSlash {
      get {
        complete(getUsers)
      }
    }
  }

  private def getUsers = ???
}

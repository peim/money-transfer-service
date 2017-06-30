package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes.{Created, InternalServerError, OK}
import akka.http.scaladsl.server.Directives.{complete, get, onComplete, pathEndOrSingleSlash, pathPrefix, post, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.peim.model.User
import com.peim.service.UsersService
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scaldi.{Injectable, Injector}

import scala.util.{Failure, Success}

class UsersServiceApi(implicit inj: Injector) extends Injectable with PlayJsonSupport {

  private val usersService = inject[UsersService]

  val route: Route = pathPrefix("users") {
    pathEndOrSingleSlash {
      get {
        onComplete(usersService.getUsers) {
          case Success(result) => complete(OK, result)
          case Failure(error) => complete(InternalServerError, error.getMessage)
        }
      } ~
        post {
          entity(as[User]) { user =>
            onComplete(usersService.createUser(user)) {
              case Success(result) => complete(Created, result)
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          }
        }
    }
  }
}

package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
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
        onComplete(usersService.findAll) {
          case Success(result) => complete(OK, result)
          case Failure(error) => complete(InternalServerError, error.getMessage)
        }
      } ~
        post {
          entity(as[User]) { user =>
            onComplete(usersService.create(user)) {
              case Success(result) => complete(Created, result)
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          }
        }
    } ~
      path(IntNumber) { id =>
        pathEndOrSingleSlash {
          get {
            onComplete(usersService.findById(id)) {
              case Success(result) => result match {
                case Some(user) => complete(OK, user)
                case None => complete(NotFound)
              }
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          } ~
            put {
              entity(as[User]) { user =>
                onComplete(usersService.update(id, user)) {
                  case Success(_) => complete(NoContent)
                  case Failure(error) => complete(InternalServerError, error.getMessage)
                }
              }
            } ~
            delete {
              onComplete(usersService.delete(id)) {
                case Success(_) => complete(NoContent)
                case Failure(error) => complete(InternalServerError, error.getMessage)
              }
            }
        }
      }
  }
}

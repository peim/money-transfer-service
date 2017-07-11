package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.peim.model.{IdWrapper, User}
import com.peim.repository.UsersRepository
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scaldi.{Injectable, Injector}

class UsersServiceApi(implicit inj: Injector) extends Injectable with PlayJsonSupport {

  private val usersRepository = inject[UsersRepository]

  val route: Route = pathPrefix("users") {
    pathEndOrSingleSlash {
      get {
        onSuccess(usersRepository.findAll) {
          result => complete(OK, result)
        }
      } ~
        post {
          entity(as[User]) { user =>
            onSuccess(usersRepository.create(user)) {
              result => complete(Created, IdWrapper(result))
            }
          }
        }
    } ~
      path(IntNumber) { id =>
        pathEndOrSingleSlash {
          get {
            onSuccess(usersRepository.findById(id)) {
              case Some(user) => complete(OK, user)
              case None => complete(NotFound)
            }
          } ~
            put {
              entity(as[User]) { user =>
                onSuccess(usersRepository.update(id, user)) {
                  _ => complete(NoContent)
                }
              }
            } ~
            delete {
              onSuccess(usersRepository.delete(id)) {
                _ => complete(NoContent)
              }
            }
        }
      }
  }
}

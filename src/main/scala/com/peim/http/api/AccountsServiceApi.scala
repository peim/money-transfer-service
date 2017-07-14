package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.peim.model.Account
import com.peim.model.response.IdWrapper
import com.peim.repository.AccountsRepository
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scaldi.{Injectable, Injector}

class AccountsServiceApi(implicit inj: Injector) extends Injectable with PlayJsonSupport {

  private val accountsRepository = inject[AccountsRepository]

  val route: Route = pathPrefix("accounts") {
    pathEndOrSingleSlash {
      get {
        onSuccess(accountsRepository.findAll) {
          result => complete(OK, result)
        }
      } ~
        post {
          entity(as[Account]) { account =>
            onSuccess(accountsRepository.create(account)) {
              result => complete(Created, IdWrapper(result))
            }
          }
        }
    } ~
      path(IntNumber) { id =>
        pathEndOrSingleSlash {
          get {
            onSuccess(accountsRepository.findById(id)) {
              case Some(account) => complete(OK, account)
              case None => complete(NotFound)
            }
          } ~
            delete {
              onSuccess(accountsRepository.delete(id)) {
                _ => complete(NoContent)
              }
            }
        }
      }
  }
}

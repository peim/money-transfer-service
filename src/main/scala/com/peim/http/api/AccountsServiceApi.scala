package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.FutureDirectives.onSuccess
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.peim.model.Account
import com.peim.service.AccountsService
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

import scala.util.{Failure, Success}

class AccountsServiceApi(val accountsService: AccountsService) extends PlayJsonSupport {

  val route: Route = pathPrefix("accounts") {
    pathEndOrSingleSlash {
      get {
        onComplete(accountsService.getAccounts) {
          case Success(result) => complete(OK, result)
          case Failure(error) => complete(InternalServerError, error.getMessage)
        }
      } ~
        post {
          entity(as[Account]) { account =>
            onComplete(accountsService.createAccount(account)) {
              case Success(result) => complete(Created, result)
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          }
        }
    }
  }
}

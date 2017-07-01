package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.peim.model.Account
import com.peim.repository.AccountsRepository
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scaldi.{Injectable, Injector}

import scala.util.{Failure, Success}

class AccountsServiceApi(implicit inj: Injector) extends Injectable with PlayJsonSupport {

  private val accountsRepository = inject[AccountsRepository]

  val route: Route = pathPrefix("accounts") {
    pathEndOrSingleSlash {
      get {
        onComplete(accountsRepository.findAll) {
          case Success(result) => complete(OK, result)
          case Failure(error) => complete(InternalServerError, error.getMessage)
        }
      } ~
        post {
          entity(as[Account]) { account =>
            onComplete(accountsRepository.create(account)) {
              case Success(result) => complete(Created, result)
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          }
        }
    } ~
      path(IntNumber) { id =>
        pathEndOrSingleSlash {
          get {
            onComplete(accountsRepository.findById(id)) {
              case Success(result) => result match {
                case Some(account) => complete(OK, account)
                case None => complete(NotFound)
              }
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          } ~
            delete {
              onComplete(accountsRepository.delete(id)) {
                case Success(_) => complete(NoContent)
                case Failure(error) => complete(InternalServerError, error.getMessage)
              }
            }
        }
      }
  }
}

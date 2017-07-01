package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.peim.model.Currency
import com.peim.repository.CurrenciesRepository
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scaldi.{Injectable, Injector}

import scala.util.{Failure, Success}

class CurrenciesServiceApi(implicit inj: Injector) extends Injectable with PlayJsonSupport {

  private val currenciesRepository = inject[CurrenciesRepository]

  val route: Route = pathPrefix("currencies") {
    pathEndOrSingleSlash {
      get {
        onComplete(currenciesRepository.findAll) {
          case Success(result) => complete(OK, result)
          case Failure(error) => complete(InternalServerError, error.getMessage)
        }
      } ~
        post {
          entity(as[Currency]) { currency =>
            onComplete(currenciesRepository.create(currency)) {
              case Success(result) => complete(Created, result)
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          }
        }
    } ~
      path(IntNumber) { id =>
        pathEndOrSingleSlash {
          get {
            onComplete(currenciesRepository.findById(id)) {
              case Success(result) => result match {
                case Some(currency) => complete(OK, currency)
                case None => complete(NotFound)
              }
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          } ~
            delete {
              onComplete(currenciesRepository.delete(id)) {
                case Success(_) => complete(NoContent)
                case Failure(error) =>
                  println(error)
                  complete(InternalServerError, error.getMessage)
              }
            }
        }
      }
  }
}

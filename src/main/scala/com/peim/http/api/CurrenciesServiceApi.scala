package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.peim.model.{Currency, IdWrapper}
import com.peim.repository.CurrenciesRepository
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scaldi.{Injectable, Injector}

class CurrenciesServiceApi(implicit inj: Injector) extends Injectable with PlayJsonSupport {

  private val currenciesRepository = inject[CurrenciesRepository]

  val route: Route = pathPrefix("currencies") {
    pathEndOrSingleSlash {
      get {
        onSuccess(currenciesRepository.findAll) {
          result => complete(OK, result)
        }
      } ~
        post {
          entity(as[Currency]) { currency =>
            onSuccess(currenciesRepository.create(currency)) {
              result => complete(Created, IdWrapper(result))
            }
          }
        }
    } ~
      path(IntNumber) { id =>
        pathEndOrSingleSlash {
          get {
            onSuccess(currenciesRepository.findById(id)) {
              case Some(currency) => complete(OK, currency)
              case None => complete(NotFound)
            }
          } ~
            delete {
              onSuccess(currenciesRepository.delete(id)) {
                _ => complete(NoContent)
              }
            }
        }
      }
  }
}

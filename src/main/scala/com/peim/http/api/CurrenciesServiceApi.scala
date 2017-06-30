package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes.{Created, InternalServerError, OK}
import akka.http.scaladsl.server.Directives.{complete, get, onComplete, pathEndOrSingleSlash, pathPrefix, post, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.peim.model.Currency
import com.peim.service.CurrenciesService
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scaldi.{Injectable, Injector}

import scala.util.{Failure, Success}

class CurrenciesServiceApi(implicit inj: Injector) extends Injectable with PlayJsonSupport {

  private val currenciesService = inject[CurrenciesService]

  val route: Route = pathPrefix("currencies") {
    pathEndOrSingleSlash {
      get {
        onComplete(currenciesService.getCurrencies) {
          case Success(result) => complete(OK, result)
          case Failure(error) => complete(InternalServerError, error.getMessage)
        }
      } ~
        post {
          entity(as[Currency]) { currency =>
            onComplete(currenciesService.createCurrency(currency)) {
              case Success(result) => complete(Created, result)
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          }
        }
    }
  }
}

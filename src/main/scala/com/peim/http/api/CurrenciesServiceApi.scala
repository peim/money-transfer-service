package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
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
        onComplete(currenciesService.findAll) {
          case Success(result) => complete(OK, result)
          case Failure(error) => complete(InternalServerError, error.getMessage)
        }
      } ~
        post {
          entity(as[Currency]) { currency =>
            onComplete(currenciesService.create(currency)) {
              case Success(result) => complete(Created, result)
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          }
        }
    } ~
      path(IntNumber) { id =>
        pathEndOrSingleSlash {
          get {
            onComplete(currenciesService.findById(id)) {
              case Success(result) => result match {
                case Some(currency) => complete(OK, currency)
                case None => complete(NotFound)
              }
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          } ~
            delete {
              onComplete(currenciesService.delete(id)) {
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

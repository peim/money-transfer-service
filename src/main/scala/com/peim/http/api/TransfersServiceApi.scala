package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.peim.model.Transfer
import com.peim.service.TransfersService
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scaldi.{Injectable, Injector}

import scala.util.{Failure, Success}

class TransfersServiceApi(implicit inj: Injector) extends Injectable with PlayJsonSupport {

  private val transfersService = inject[TransfersService]

  val route: Route = pathPrefix("transfers") {
    pathEndOrSingleSlash {
      get {
        onComplete(transfersService.findAll) {
          case Success(result) => complete(OK, result)
          case Failure(error) => complete(InternalServerError, error.getMessage)
        }
      } ~
        post {
          entity(as[Transfer]) { transfer =>
            onComplete(transfersService.create(transfer)) {
              case Success(result) => complete(Created, result)
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          }
        }
    } ~
      path(IntNumber) { id =>
        pathEndOrSingleSlash {
          get {
            onComplete(transfersService.findById(id)) {
              case Success(result) => result match {
                case Some(transfer) => complete(OK, transfer)
                case None => complete(NotFound)
              }
              case Failure(error) => complete(InternalServerError, error.getMessage)
            }
          }
        }
      }
  }
}

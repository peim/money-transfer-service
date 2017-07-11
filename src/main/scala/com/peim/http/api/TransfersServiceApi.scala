package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.peim.model.{IdWrapper, Transfer}
import com.peim.repository.TransfersRepository
import com.peim.service.TransfersService
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scaldi.{Injectable, Injector}

import scala.util.{Failure, Success}

class TransfersServiceApi(implicit inj: Injector) extends Injectable with PlayJsonSupport {

  private val transfersRepository = inject[TransfersRepository]
  private val transfersService = inject[TransfersService]

  val route: Route = pathPrefix("transfers") {
    pathEndOrSingleSlash {
      get {
        onSuccess(transfersRepository.findAll) {
          result => complete(OK, result)
        }
      } ~
        post {
          entity(as[Transfer]) { transfer =>
            transfersService.createTransfer(transfer) match {
              case Success(result) => complete(Created, IdWrapper(result))
              case Failure(error) => complete(InternalServerError, error)
            }
          }
        }
    } ~
      path(IntNumber) { id =>
        pathEndOrSingleSlash {
          get {
            onSuccess(transfersRepository.findById(id)) {
              case Some(transfer) => complete(OK, transfer)
              case None => complete(NotFound)
            }
          }
        }
      }
  }
}

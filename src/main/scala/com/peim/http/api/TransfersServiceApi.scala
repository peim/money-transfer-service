package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.peim.model.Transfer
import com.peim.service.{AccountsService, TransfersService}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scaldi.{Injectable, Injector}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

import scala.concurrent.ExecutionContext.Implicits.global

class TransfersServiceApi(implicit inj: Injector) extends Injectable with PlayJsonSupport {

  private val transfersService = inject[TransfersService]
  private val accountsService = inject[AccountsService]

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
            onComplete(createTransfer(transfer)) {
              case Success(res) => res match {
                case Success(result) => complete(Created, result)
                case Failure(error) => complete(InternalServerError, error.getMessage)
              }
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

  private def createTransfer(transfer: Transfer): Future[Try[Int]] = {
    for {
      src <- accountsService.findById(transfer.sourceAccountId)
      dest <- accountsService.findById(transfer.destAccountId)
      res <- (src, dest) match {
        case (Some(srcAcc), Some(destAcc)) =>
          if (srcAcc.balance >= transfer.sum) {
            val debit = srcAcc.balance - transfer.sum
            val credit = destAcc.balance + transfer.sum
            transfersService.create(transfer, debit, credit).map(Try(_))
          }
          else Future(Failure(new RuntimeException(s"Недостаточно средств на счете списания")))
        case (Some(_), None) => Future(Failure(
          new RuntimeException(s"Счет зачисления с id ${transfer.destAccountId} не найден")))
        case (None, Some(_)) => Future(Failure(
          new RuntimeException(s"Счет списания с id ${transfer.sourceAccountId} не найден")))
        case (None, None) => Future(Failure(
          new RuntimeException(s"Счета списания и зачисления не найдены")))
      }
    } yield res
  }
}

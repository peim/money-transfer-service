package com.peim.repository

import java.time.OffsetDateTime

import com.peim.model.table._
import com.peim.model.{Canceled, Processing, Transfer, TransferStatus}
import com.peim.utils.DatabaseService
import scaldi.{Injectable, Injector}
import slick.dbio.DBIOAction
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class TransfersRepository(implicit inj: Injector, executionContext: ExecutionContext) extends Injectable {

  import Transfer._

  private val db = inject[DatabaseService].db

  def findAll: Future[Seq[Transfer]] = db.run(transfers.result)

  def findById(id: Int): Future[Option[Transfer]] =
    db.run(transfers.filter(_.id === id).result.headOption)

  def create(transfer: Transfer): Future[Int] = {
    val createTransfer = (for {
      a <- accounts.filter(e => e.id === transfer.sourceAccountId &&
        e.currencyId === transfer.currencyId &&
        e.balance >= transfer.sum).result.headOption
      b <- a match {
        case Some(acc) =>
          val newBalance = acc.balance - transfer.sum
          accounts.filter(_.id === transfer.sourceAccountId).map(_.balance).update(newBalance)
            .andThen(transfers returning transfers.map(_.id) += transfer)
        case None =>
          DBIOAction.failed(new RuntimeException(s"Appropriate source account by id ${transfer.sourceAccountId} not found"))
      }
    } yield b).transactionally

    db.run(createTransfer)
  }

  def approve(transfer: Transfer): Future[Int] = {
    val approveTransfer = (for {
      a <- accounts.filter(e => e.id === transfer.destAccountId &&
        e.currencyId === transfer.currencyId).result.headOption
      b <- a match {
        case Some(acc) =>
          val newBalance = acc.balance + transfer.sum
          accounts.filter(_.id === transfer.destAccountId).map(_.balance).update(newBalance)
            .andThen(transfers.filter(_.id === transfer.id).map(_.status).update(transfer.status))
        case None =>
          DBIOAction.failed(new RuntimeException(s"Appropriate dest account by id ${transfer.destAccountId} not found"))
      }
    } yield b).transactionally

    db.run(approveTransfer)
  }

  def rollback(limitDateTime: OffsetDateTime): Future[Seq[Int]] = {
    val rollbackTransfer = (for {
      a <- (transfers join accounts on {
        case (i1, i2) => i1.sourceAccountId === i2.id &&
          i1.status === Processing.asInstanceOf[TransferStatus] &&
          i1.created <= limitDateTime
      }).result
      b <- DBIO.sequence(
        a.groupBy(_._2.id).map(e => e._1 -> e._2.map {
          case (transfer, account) => account.balance -> transfer.sum
        }).map(e => (e._1, e._2.map(_._1).head) -> e._2.map(_._2).sum).toSeq.map {
          case ((accountId, balance), transfersSum) =>
            accounts.filter(_.id === accountId).map(_.balance).update(balance + transfersSum)
              .andThen(transfers.filter(_.status === Processing.asInstanceOf[TransferStatus])
                .map(_.status).update(Canceled))
        })
    } yield b).transactionally

    db.run(rollbackTransfer)
  }

}

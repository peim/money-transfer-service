package com.peim.service

import java.time.OffsetDateTime
import java.util.concurrent.locks.ReentrantLock

import com.peim.model.Transfer
import com.peim.repository.TransfersRepository
import scaldi.{Injectable, Injector}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class TransfersService(implicit inj: Injector, executionContext: ExecutionContext) extends Injectable {

  private val lock = new ReentrantLock()
  private val transfersRepository = inject[TransfersRepository]

  def createTransfer(transfer: Transfer): Future[Try[Int]] = {
    lock.lock()
    val transferId = try {
      Await.result(transfersRepository.create(transfer), 20.seconds)
    } finally {
      lock.unlock()
    }
    Try(transferId) match {
      case Success(newId) =>
        val createdTransfer = transfer.approved(newId)
        transfersRepository.approve(createdTransfer)
          .map(e => Try(e))
          .recover {
            case e: Exception => Failure(e)
          }
      case Failure(error) => Future(Failure(error))
    }
  }

  def rollbackFailedTransfers(): Runnable = new Runnable {
    override def run(): Unit = transfersRepository.rollback(OffsetDateTime.now().minusMinutes(1))
  }
}

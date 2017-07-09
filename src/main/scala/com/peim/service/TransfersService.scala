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

  private val createLock = new ReentrantLock()
  private val approveLock = new ReentrantLock()
  private val transfersRepository = inject[TransfersRepository]

  def createTransfer(transfer: Transfer): Future[Try[Int]] = {
    createLock.lock()
    val transferId = try {
      Await.result(transfersRepository.create(transfer), 20.seconds)
    } finally {
      createLock.unlock()
    }
    Try(transferId) match {
      case Success(newId) =>
        approveLock.lock()
        try {
          val createdTransfer = transfer.approved(newId)
          val res = Await.result(transfersRepository.approve(createdTransfer), 20.seconds)
          Future(Try(res))
        } finally {
          approveLock.unlock()
        }
      case Failure(error) => Future(Failure(error))
    }
  }

  def rollbackFailedTransfers(): Runnable = new Runnable {
    private val rollbackLock = new ReentrantLock()
    override def run(): Unit = {
      rollbackLock.lock()
      try {
        Await.result(transfersRepository.rollback(OffsetDateTime.now().minusSeconds(10)), 20.seconds)
      } finally {
        rollbackLock.unlock()
      }
    }
  }
}

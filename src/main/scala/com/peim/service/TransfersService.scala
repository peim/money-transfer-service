package com.peim.service

import com.peim.model.Transfer
import com.peim.repository.{AccountsRepository, TransfersRepository}
import scaldi.{Injectable, Injector}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Try}

class TransfersService(implicit inj: Injector, executionContext: ExecutionContext) extends Injectable {

  private val transfersRepository = inject[TransfersRepository]
  private val accountsRepository = inject[AccountsRepository]

  def createTransfer(transfer: Transfer): Future[Try[Int]] = {
    for {
      src <- accountsRepository.findById(transfer.sourceAccountId)
      dest <- accountsRepository.findById(transfer.destAccountId)
      res <- (src, dest) match {
        case (Some(srcAcc), Some(destAcc)) =>
          if (srcAcc.currencyId == destAcc.currencyId) {
            if (srcAcc.balance >= transfer.sum) {
              val debit = srcAcc.balance - transfer.sum
              val credit = destAcc.balance + transfer.sum
              transfersRepository.create(transfer, debit, credit).map(Try(_))
            } else Future(Failure(
              new RuntimeException(s"Недостаточно средств на счете списания id ${transfer.destAccountId}")))
          } else Future(Failure(
            new RuntimeException(s"Валюта счетов списания и зачисления должна быть одинаковой")))
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

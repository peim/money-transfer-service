package com.peim.service

import java.time.OffsetDateTime

import com.peim.BaseServiceTest
import com.peim.model.{Processing, Transfer}
import com.peim.repository.{AccountsRepository, TransfersRepository}
import com.peim.utils.BootData
import org.scalatest.concurrent.ScalaFutures

import scala.util.Random

class TransfersServiceSpec extends BaseServiceTest with ScalaFutures {

  private val accountsRepository = inject[AccountsRepository]
  private val transfersService = inject[TransfersService]

  private val transfersRepository = inject[TransfersRepository]

  def getAccountsId: (Int, Int) = {
    val id1 = Random.nextInt(5) + 1
    val id2 = Random.nextInt(5) + 1
    if (id1 == id2)
      if (id2 == 1) (id1, id2 + 1)
      else (id1, id2 - 1)
    else (id1, id2)
  }

  "Transfers service" should {

    "check amount on all accounts [before]" in {
      whenReady(accountsRepository.findAll) { result =>
        result should have size 10
        result.map(_.balance).sum should be(5500)
      }
    }

    "emulate money transfer service" in {
      for (id <- 1 to 1000) {
        val (sourceAccountId, destAccountId) = getAccountsId
        val currencyId = BootData.getAccounts
          .find(_.id == sourceAccountId).map(_.currencyId).get
        val sum = Random.nextInt(100) + 1
        val created = OffsetDateTime.now()
        val transfer = Transfer(id, sourceAccountId, destAccountId, currencyId, sum, created, Processing)
        try {
          transfersService.createTransfer(transfer)
        } catch {
          case _: Exception =>
        }
      }
    }

    "check amount on all accounts [after]" in {
      Thread.sleep(20 * 1000)
      transfersService.rollbackFailedTransfers().run()
      Thread.sleep(100)
      whenReady(transfersRepository.findAll) { result =>
        result.map(_.status) should not contain Processing
      }
      whenReady(accountsRepository.findAll) { result =>
        result should have size 10
        result.map(_.balance).sum should be(5500)
      }
    }
  }
}

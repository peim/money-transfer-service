package com.peim.api

import java.time.OffsetDateTime

import akka.http.scaladsl.model.StatusCodes.{Created, InternalServerError, OK}
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import com.peim.BaseServiceTest
import com.peim.model.{Processing, Transfer, TransferView}
import com.peim.repository.{AccountsRepository, TransfersRepository}
import com.peim.utils.BootData
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.Json

class TransfersServiceApiSpec extends BaseServiceTest with ScalaFutures {

  private val accounts = BootData.getAccounts
  private val transfers = BootData.getTransfers
  private val accountsRepository = inject[AccountsRepository]
  private val transfersRepository = inject[TransfersRepository]
  private val route = httpService.transfersRouter

  "Transfers service" should {

    "retrieve transfers list" in {
      Get("/transfers") ~> route ~> check {
        status should be(OK)
        responseAs[Seq[Transfer]] should have size 3
        responseAs[Seq[Transfer]].map(TransferView.apply) should
          be(BootData.getTransfers.map(TransferView.apply))
      }
    }

    "retrieve transfer by id" in {
      val transfer = transfers.find(_.id == 1).get
      Get(s"/transfers/${transfer.id}") ~> route ~> check {
        TransferView.apply(responseAs[Transfer]) should be(TransferView.apply(transfer))
      }
    }

    "successful create transfer" in {
      val sourceAccount = accounts.find(_.id == 1).get
      val destAccount = accounts.find(_.id == 3).get
      val newTransfer = Transfer(4, sourceAccount.id, destAccount.id, sourceAccount.currencyId,
        100, OffsetDateTime.now(), Processing)
      val approvedTransfer = newTransfer.approved(newTransfer.id)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, Json.toJson(newTransfer).toString)
      Post(s"/transfers", requestEntity) ~> route ~> check {
        status should be(Created)
        whenReady(transfersRepository.findById(newTransfer.id)) { result =>
          result.map(TransferView.apply) should be(Some(TransferView.apply(approvedTransfer)))
        }
        whenReady(accountsRepository.findById(sourceAccount.id)) { result =>
          result.map(_.balance) should be(Some(sourceAccount.balance - newTransfer.sum))
        }
        whenReady(accountsRepository.findById(destAccount.id)) { result =>
          result.map(_.balance) should be(Some(destAccount.balance + newTransfer.sum))
        }
      }
    }

    "fail create transfer [first transaction]" in {
      val sourceAccount = accounts.find(_.id == 4).get
      val destAccount = accounts.find(_.id == 5).get
      val newTransfer = Transfer(5, sourceAccount.id, destAccount.id, sourceAccount.currencyId,
        1000, OffsetDateTime.now(), Processing)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, Json.toJson(newTransfer).toString)
      Post(s"/transfers", requestEntity) ~> route ~> check {
        status should be(InternalServerError)
        whenReady(transfersRepository.findById(newTransfer.id)) { result =>
          result should be(Option.empty[Transfer])
        }
        whenReady(accountsRepository.findById(sourceAccount.id)) { result =>
          result.map(_.balance) should be(Some(sourceAccount.balance))
        }
        whenReady(accountsRepository.findById(destAccount.id)) { result =>
          result.map(_.balance) should be(Some(destAccount.balance))
        }
      }
    }

    "fail create transfer [second transaction]" in {
      val sourceAccount = accounts.find(_.id == 4).get
      val destAccount = accounts.find(_.id == 2).get
      val newTransfer = Transfer(5, sourceAccount.id, destAccount.id, sourceAccount.currencyId,
        200, OffsetDateTime.now(), Processing)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, Json.toJson(newTransfer).toString)
      Post(s"/transfers", requestEntity) ~> route ~> check {
        status should be(InternalServerError)
        whenReady(transfersRepository.findById(newTransfer.id)) { result =>
          result.map(TransferView.apply) should be(Some(TransferView.apply(newTransfer)))
        }
        whenReady(accountsRepository.findById(sourceAccount.id)) { result =>
          result.map(_.balance) should be(Some(sourceAccount.balance - newTransfer.sum))
        }
        whenReady(accountsRepository.findById(destAccount.id)) { result =>
          result.map(_.balance) should be(Some(destAccount.balance))
        }
      }
    }
  }
}

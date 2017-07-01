package com.peim.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import com.peim.BaseServiceTest
import com.peim.model.Transfer
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
        responseAs[Seq[Transfer]] should be(BootData.getTransfers)
      }
    }

    "retrieve transfer by id" in {
      val transfer = transfers.find(_.id == 1).get
      Get(s"/transfers/${transfer.id}") ~> route ~> check {
        responseAs[Transfer] should be(transfer)
      }
    }

    "successful create transfer" in {
      val sourceAccount = accounts.find(_.id == 1).get
      val destAccount = accounts.find(_.id == 3).get
      val newTransfer = Transfer(4, sourceAccount.id, destAccount.id, 100)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, Json.toJson(newTransfer).toString)
      Post(s"/transfers", requestEntity) ~> route ~> check {
        status should be(Created)
        responseAs[Int] should be(newTransfer.id)
        whenReady(transfersRepository.findById(newTransfer.id)) { result =>
          result should be(Some(newTransfer))
        }
        whenReady(accountsRepository.findById(sourceAccount.id)) { result =>
          result.map(_.balance) should be(Some(sourceAccount.balance - newTransfer.sum))
        }
        whenReady(accountsRepository.findById(destAccount.id)) { result =>
          result.map(_.balance) should be(Some(destAccount.balance + newTransfer.sum))
        }
      }
    }

    "fail create transfer" in {
      val sourceAccount = accounts.find(_.id == 4).get
      val destAccount = accounts.find(_.id == 5).get
      val newTransfer = Transfer(5, sourceAccount.id, destAccount.id, 1000)
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
  }
}

package com.peim.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import com.peim.BaseServiceTest
import com.peim.model.Transfer
import com.peim.service.TransfersService
import com.peim.utils.BootData
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.Json

class TransfersServiceApiSpec extends BaseServiceTest with ScalaFutures {

  private val transfers = BootData.getTransfers
  private val transfersService = inject[TransfersService]
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

    "create transfer" in {
      val newTransfer = Transfer(4, 1, 3, 100)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, Json.toJson(newTransfer).toString)
      Post(s"/transfers", requestEntity) ~> route ~> check {
        status should be(Created)
        responseAs[Int] should be(newTransfer.id)
        whenReady(transfersService.findById(newTransfer.id)) { result =>
          result should be(Some(newTransfer))
        }
      }
    }
  }
}

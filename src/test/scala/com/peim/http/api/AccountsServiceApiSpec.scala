package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import com.peim.model.Account
import com.peim.utils.BootData
import org.scalatest.concurrent.ScalaFutures

class AccountsServiceApiSpec extends BaseServiceTest with ScalaFutures {

  val route = httpService.accountsRouter

  "Accounts service" should {

    "retrieve accounts list" in {
      Get("/accounts") ~> route ~> check {
        status should be(OK)
        responseAs[Seq[Account]] should have size 3
        responseAs[Seq[Account]] should be(BootData.getAccounts)
      }
    }
  }
}

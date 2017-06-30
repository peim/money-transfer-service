package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import com.peim.model.Currency
import com.peim.utils.BootData
import org.scalatest.concurrent.ScalaFutures

class CurrenciesServiceApiSpec extends BaseServiceTest with ScalaFutures {

  val route = httpService.currenciesRouter

  "Currencies service" should {

    "retrieve currencies list" in {
      Get("/currencies") ~> route ~> check {
        status should be(OK)
        responseAs[Seq[Currency]] should have size 3
        responseAs[Seq[Currency]] should be(BootData.getCurrencies)
      }
    }
  }
}

package com.peim.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import com.peim.BaseServiceTest
import com.peim.model.{Currency, IdWrapper}
import com.peim.repository.CurrenciesRepository
import com.peim.utils.BootData
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.Json

class CurrenciesServiceApiSpec extends BaseServiceTest with ScalaFutures {

  private val currencies = BootData.getCurrencies
  private val currenciesRepository = inject[CurrenciesRepository]
  private val route = httpService.currenciesRouter

  "Currencies service" should {

    "retrieve currencies list" in {
      Get("/currencies") ~> route ~> check {
        status should be(OK)
        responseAs[Seq[Currency]] should have size 3
        responseAs[Seq[Currency]] should be(BootData.getCurrencies)
      }
    }

    "retrieve currency by id" in {
      val currency = currencies.find(_.id == 1).get
      Get(s"/currencies/${currency.id}") ~> route ~> check {
        responseAs[Currency] should be(currency)
      }
    }

    "create currency" in {
      val newCurrency = Currency(4, "RUR")
      val requestEntity = HttpEntity(MediaTypes.`application/json`, Json.toJson(newCurrency).toString)
      Post(s"/currencies", requestEntity) ~> route ~> check {
        status should be(Created)
        responseAs[IdWrapper] should be(IdWrapper(newCurrency.id))
        whenReady(currenciesRepository.findById(newCurrency.id)) { result =>
          result should be(Some(newCurrency))
        }
      }
    }

    "delete currency" in {
      val id = 4
      Delete(s"/currencies/$id") ~> route ~> check {
        response.status should be(NoContent)
        whenReady(currenciesRepository.findById(id)) { result =>
          result should be(Option.empty[Currency])
        }
      }
    }
  }
}

package com.peim.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import com.peim.BaseServiceTest
import com.peim.model.Account
import com.peim.service.AccountsService
import com.peim.utils.BootData
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.Json

class AccountsServiceApiSpec extends BaseServiceTest with ScalaFutures {

  private val accounts = BootData.getAccounts
  private val accountsService = inject[AccountsService]
  private val route = httpService.accountsRouter

  "Accounts service" should {

    "retrieve accounts list" in {
      Get("/accounts") ~> route ~> check {
        status should be(OK)
        responseAs[Seq[Account]] should have size 5
        responseAs[Seq[Account]] should be(BootData.getAccounts)
      }
    }

    "retrieve account by id" in {
      val account = accounts.find(_.id == 1).get
      Get(s"/accounts/${account.id}") ~> route ~> check {
        responseAs[Account] should be(account)
      }
    }

    "create account" in {
      val newAccount = Account(6, 1, 3, 300)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, Json.toJson(newAccount).toString)
      Post(s"/accounts", requestEntity) ~> route ~> check {
        status should be(Created)
        responseAs[Int] should be(newAccount.id)
        whenReady(accountsService.findById(newAccount.id)) { result =>
          result should be(Some(newAccount))
        }
      }
    }

    "delete account" in {
      val id = 6
      Delete(s"/accounts/$id") ~> route ~> check {
        response.status should be(NoContent)
        whenReady(accountsService.findById(id)) { result =>
          result should be(Option.empty[Account])
        }
      }
    }
  }
}
package com.peim.http.api

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.peim.http.HttpService
import com.peim.service.{AccountsService, CurrenciesService, UsersService}
import com.peim.utils.{BootData, DatabaseService}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import org.scalatest._
import scaldi.{Injectable, Module}

trait BaseServiceTest extends WordSpec with Matchers with ScalatestRouteTest with Injectable with PlayJsonSupport {

  implicit val testModule = new Module {
    bind[DatabaseService] to new DatabaseService()
    bind[AccountsService] to new AccountsService()
    bind[UsersService] to new UsersService()
    bind[CurrenciesService] to new CurrenciesService()
  }

  new BootData().load()
  val httpService = new HttpService()
}

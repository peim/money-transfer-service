package com.peim

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.peim.http.HttpService
import com.peim.repository._
import com.peim.service.TransfersService
import com.peim.utils.{BootData, DatabaseService}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import org.scalatest._
import scaldi.{Injectable, Module}

trait BaseServiceTest extends WordSpec
  with Matchers
  with ScalatestRouteTest
  with Injectable
  with PlayJsonSupport {

  implicit val testModule = new Module {
    bind[DatabaseService] to new DatabaseService()

    bind[AccountsRepository] to new AccountsRepository()
    bind[UsersRepository] to new UsersRepository()
    bind[CurrenciesRepository] to new CurrenciesRepository()
    bind[TransfersRepository] to new TransfersRepository()

    bind[TransfersService] to new TransfersService()
  }

  new BootData().load()
  val httpService = new HttpService()
}

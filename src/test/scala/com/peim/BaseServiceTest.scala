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
  with BeforeAndAfterAll
  with ScalatestRouteTest
  with Injectable
  with PlayJsonSupport {

  val database =  new DatabaseService()

  implicit val testModule = new Module {
    bind[DatabaseService] to database

    bind[AccountsRepository] to new AccountsRepository()
    bind[UsersRepository] to new UsersRepository()
    bind[CurrenciesRepository] to new CurrenciesRepository()
    bind[TransfersRepository] to new TransfersRepository()

    bind[TransfersService] to new TransfersService()
  }

  private val data = new BootData()

  val httpService = new HttpService()

  override def beforeAll(): Unit = {
    data.load()
    Thread.sleep(500)
  }

  override def afterAll(): Unit = {
    data.clean()
    Thread.sleep(500)
  }
}

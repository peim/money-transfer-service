package com.peim.http.api

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.peim.http.HttpService
import com.peim.service.{AccountsService, CurrenciesService, UsersService}
import com.peim.utils.DatabaseService
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import org.scalatest._

trait BaseServiceTest extends WordSpec with Matchers with ScalatestRouteTest with PlayJsonSupport {

  private val databaseService = new DatabaseService

  private val accountsService = new AccountsService(databaseService)
  private val usersService = new UsersService(databaseService)
  private val currenciesService = new CurrenciesService(databaseService)

  val httpService = new HttpService(accountsService, usersService, currenciesService)

}

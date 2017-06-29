package com.peim.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.http.api.{AccountsServiceApi, CurrenciesServiceApi, UsersServiceApi}
import com.peim.service.{AccountsService, CurrenciesService, UsersService}

import scala.concurrent.ExecutionContext

class HttpService(accountsService: AccountsService,
                  usersService: UsersService,
                  currenciesService: CurrenciesService)(implicit executionContext: ExecutionContext) {

  private val accountsRouter = new AccountsServiceApi(accountsService).route
  private val usersRouter = new UsersServiceApi(usersService).route
  private val currenciesRouter = new CurrenciesServiceApi(currenciesService).route

  val routes: Route =
    pathPrefix("v1") {
      accountsRouter ~ usersRouter ~ currenciesRouter
    }

}
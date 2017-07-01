package com.peim.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.peim.http.api._
import scaldi.{Injectable, Injector}

class HttpService(implicit inj: Injector) extends Injectable {

  val accountsRouter = new AccountsServiceApi().route
  val usersRouter = new UsersServiceApi().route
  val currenciesRouter = new CurrenciesServiceApi().route
  val transfersRouter = new TransfersServiceApi().route

  val routes: Route = accountsRouter ~ usersRouter ~ currenciesRouter ~ transfersRouter
}

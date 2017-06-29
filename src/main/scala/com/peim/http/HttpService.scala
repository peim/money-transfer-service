package com.peim.http

import akka.http.scaladsl.server.Directives.pathPrefix
import com.peim.http.api.{AccountsServiceApi, UsersServiceApi}
import akka.http.scaladsl.server.Directives._
import com.peim.service.AccountsService

import scala.concurrent.ExecutionContext

class HttpService(accountsService: AccountsService)(implicit executionContext: ExecutionContext) {

  val usersRouter = new UsersServiceApi().route
  val accountsRouter = new AccountsServiceApi(accountsService).route

  val routes =
    pathPrefix("v1") {
        usersRouter ~
          accountsRouter
    }

}
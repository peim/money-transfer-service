package com.peim.http

import akka.http.scaladsl.server.Directives.pathPrefix
import com.peim.http.api.{AccountsServiceApi, UsersServiceApi}

import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContext

class HttpService(implicit executionContext: ExecutionContext) {

  val usersRouter = new UsersServiceApi().route
  val accountsRouter = new AccountsServiceApi().route

  val routes =
    pathPrefix("v1") {
        usersRouter ~
          accountsRouter
    }

}
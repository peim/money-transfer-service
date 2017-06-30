package com.peim.service

import com.peim.model.Account
import com.peim.model.table._
import com.peim.utils.DatabaseService
import scaldi.{Injectable, Injector}
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class AccountsService(implicit inj: Injector, executionContext: ExecutionContext) extends Injectable {

  private val databaseService = inject[DatabaseService]

  import databaseService._

  def getAccounts: Future[Seq[Account]] = db.run(accounts.result)

  def createAccount(account: Account): Future[Int] =
    db.run(accounts returning accounts.map(_.id) += account)
}

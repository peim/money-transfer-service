package com.peim.service

import com.peim.model.Account
import com.peim.model.table.AccountsTable
import com.peim.utils.DatabaseService
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class AccountsService(val databaseService: DatabaseService)(implicit executionContext: ExecutionContext) extends AccountsTable {

  import databaseService._

  def getAccounts: Future[Seq[Account]] = db.run(accounts.result)

  def createAccount(account: Account): Future[Account] =
    db.run(accounts returning accounts += account)

}

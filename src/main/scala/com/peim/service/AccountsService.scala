package com.peim.service

import com.peim.model.Account
import com.peim.model.table._
import com.peim.utils.{BootData, DatabaseService}
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class AccountsService(val databaseService: DatabaseService)(implicit executionContext: ExecutionContext) {

  import databaseService._

  def getAccounts: Future[Seq[Account]] = db.run(accounts.result)

  def createAccount(account: Account): Future[Int] =
    db.run(accounts returning accounts.map(_.id) += account)

  private val setup = DBIO.seq(
    accounts.schema.create,
    accounts ++= BootData.getAccounts
  )

  db.run(setup)
}

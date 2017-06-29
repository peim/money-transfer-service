package com.peim.service

import com.peim.model.User
import com.peim.model.table.UsersTable
import com.peim.utils.DatabaseService
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class UsersService(val databaseService: DatabaseService)(implicit executionContext: ExecutionContext) extends UsersTable {

  import databaseService._

  def getAccounts: Future[Seq[User]] = db.run(users.result)

  def createAccount(user: User): Future[User] =
    db.run(users returning users += user)

}
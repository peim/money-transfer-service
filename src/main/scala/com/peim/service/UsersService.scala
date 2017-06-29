package com.peim.service

import java.util.UUID

import com.peim.model.User
import com.peim.model.table.UsersTable
import com.peim.utils.DatabaseService
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class UsersService(val databaseService: DatabaseService)(implicit executionContext: ExecutionContext) extends UsersTable {

  import databaseService._

  def getUsers: Future[Seq[User]] = db.run(users.result)

  def createUser(user: User): Future[User] =
    db.run(users returning users += user)

  private val setup = DBIO.seq(
    users.schema.create,

    users += User(1, "Alex"),
    users += User(2, "Max"),
    users += User(3, "Fred")
  )

  db.run(setup)
}

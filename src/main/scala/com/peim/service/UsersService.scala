package com.peim.service

import com.peim.model.User
import com.peim.model.table._
import com.peim.utils.DatabaseService
import slick.driver.H2Driver.api._

import scala.concurrent.{ExecutionContext, Future}

class UsersService(val databaseService: DatabaseService)(implicit executionContext: ExecutionContext) {

  import databaseService._

  def getUsers: Future[Seq[User]] = db.run(users.result)

  def createUser(user: User): Future[Int] =
    db.run(users returning users.map(_.id) += user)

  private val setup = DBIO.seq(
    users.schema.create,

    users += User(1, "Alex"),
    users += User(2, "Max"),
    users += User(3, "Fred")
  )

  db.run(setup)
}

package com.peim.model.table

import java.util.UUID

import com.peim.model.User
import com.peim.utils.AdvancedH2Driver.api._
import slick.lifted.Tag

trait UsersTable {

  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[UUID]("id")
    def name = column[String]("name")

    def * = (id, name) <> (User.tupled, User.unapply)

    def pkUsers = primaryKey("pk_users", id)
  }

  protected val users = TableQuery[Users]
}

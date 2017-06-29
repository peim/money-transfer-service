package com.peim.model.table

import com.peim.model.User
import slick.driver.H2Driver.api._
import slick.lifted.Tag

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Int]("id", O.PrimaryKey)
  def name = column[String]("name")

  def * = (id, name) <> (User.tupled, User.unapply)
}

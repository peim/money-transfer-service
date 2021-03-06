package com.peim.model

import play.api.libs.json.{Format, Json}

case class User(id: Int, name: String) {

  def setName(name: String): User = {
    User(id, name)
  }
}

object User extends ((Int, String) => User) {
  implicit val userFormat: Format[User] = Json.format[User]
}

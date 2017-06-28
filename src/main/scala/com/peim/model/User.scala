package com.peim.model

import java.util.UUID

import play.api.libs.json.{Format, Json}

case class User(id: UUID, name: String)

object User extends ((UUID, String) => User) {
  implicit val userFormat: Format[User] = Json.format[User]
}

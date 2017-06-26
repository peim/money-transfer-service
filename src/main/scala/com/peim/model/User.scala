package com.peim.model

import java.util.UUID

import play.api.libs.json.{Format, Json}

case class User(id: UUID, name: String)

object User {
  implicit val userFormat: Format[User] = Json.format[User]
}

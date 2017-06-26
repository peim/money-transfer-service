package com.peim.model

import java.util.UUID

import play.api.libs.json.{Format, Json}

class Account(id: UUID, userId: UUID, currentId: UUID, balance: Double)

object Account {
  implicit val accountFormat: Format[Account] = Json.format[Account]
}
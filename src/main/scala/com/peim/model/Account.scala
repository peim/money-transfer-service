package com.peim.model

import java.util.UUID

import play.api.libs.json.{Format, Json}

case class Account(id: UUID, userId: UUID, currencyId: Int, balance: Double)

object Account extends ((UUID, UUID, Int, Double) => Account) {
  implicit val accountFormat: Format[Account] = Json.format[Account]
}

package com.peim.model

import play.api.libs.json.{Format, Json}

case class Account(id: Int, userId: Int, currencyId: Int, balance: Double)

object Account extends ((Int, Int, Int, Double) => Account) {
  implicit val accountFormat: Format[Account] = Json.format[Account]
}

package com.peim.model

import play.api.libs.json.{Format, Json}

case class Account(id: Int, userId: Int, currencyId: Int, balance: Double) {
  require(balance >= 0, "balance must be greater than or equal to 0")
}

object Account extends ((Int, Int, Int, Double) => Account) {
  implicit val accountFormat: Format[Account] = Json.format[Account]
}

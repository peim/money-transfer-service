package com.peim.model

import slick.lifted.TableQuery

package object table {

  val accounts: TableQuery[Accounts] = TableQuery[Accounts]

  val currencies: TableQuery[Currencies] = TableQuery[Currencies]

  val users:TableQuery[Users] = TableQuery[Users]
}

package com.peim.utils

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

class DatabaseService {
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("database")
  val db = dbConfig.db
  db.createSession()
}

package com.peim.utils

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()

  val httpHost: String = config.getString("http.host")
  val httpPort: Int = config.getInt("http.port")

//  val driver: String = config.getString("database.db.driver")
//  val url: String = config.getString("database.db.url")
//  val user: String = config.getString("database.db.user")
//  val password: String = config.getString("database.db.password")
}

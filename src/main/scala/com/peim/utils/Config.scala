package com.peim.utils

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()

  val httpHost: String = config.getString("http.host")
  val httpPort: Int = config.getInt("http.port")
}

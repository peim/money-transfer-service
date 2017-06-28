package com.peim.utils

import com.github.tminglei.slickpg.{ExPostgresDriver, PgArraySupport, PgDate2Support, PgPlayJsonSupport}
import slick.driver.JdbcProfile
import slick.profile.Capability

class AdvancedH2Driver extends ExPostgresDriver
  with PgArraySupport
  with PgDate2Support
  with PgPlayJsonSupport {

  def pgjson = "jsonb"

  override protected def computeCapabilities: Set[Capability] =
    super.computeCapabilities + JdbcProfile.capabilities.insertOrUpdate

  override val api = MyAPI

  object MyAPI extends API
    with SimpleArrayImplicits
    with SimpleArrayPlainImplicits
    with DateTimeImplicits
    with Date2DateTimePlainImplicits
    with PlayJsonImplicits
    with PlayJsonPlainImplicits
}

object AdvancedH2Driver extends AdvancedH2Driver

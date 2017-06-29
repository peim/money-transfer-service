package com.peim.utils

import java.sql.{Connection, DriverManager}

import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor

class DatabaseMigration {

  val changeLogFile = "migrations/changelog.xml"

  def createLiquibase(dbConnection: Connection): Liquibase = {
    val database = DatabaseFactory.getInstance.findCorrectDatabaseImplementation(new JdbcConnection(dbConnection))
    val classLoader = classOf[DatabaseMigration].getClassLoader
    val resourceAccessor = new ClassLoaderResourceAccessor(classLoader)
    new Liquibase(changeLogFile, resourceAccessor, database)
  }

  def migrate(driver: String, url: String, user: String, password: String): Unit = {
    Class.forName(driver)
    val dbConnection = DriverManager.getConnection(url, user, password)
    val liquibase = createLiquibase(dbConnection)
    try {
      liquibase.update("MIGRATION")
    } catch {
      case e: Throwable => throw e
    } finally {
      liquibase.forceReleaseLocks()
      dbConnection.rollback()
      dbConnection.close()
    }
  }
}

object DatabaseMigration extends DatabaseMigration

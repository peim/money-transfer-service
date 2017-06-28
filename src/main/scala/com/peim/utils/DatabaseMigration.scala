package com.peim.utils

import java.sql.{Connection, DriverManager}

import com.typesafe.config.ConfigFactory
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

  def migrate(): Unit = {
    val dbConnection = getConnection
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

  def getConnection = {
    val config = ConfigFactory.load()

    val url: String = config.getString("database.db.url")
    val driver: String = config.getString("database.db.driver")
    val user: String = "sa"//config.getString("database.db.user")
    val password: String = ""//config.getString("database.db.password")

    Class.forName(driver)
    DriverManager.getConnection(url, user, password)
  }
}

object DatabaseMigration extends DatabaseMigration

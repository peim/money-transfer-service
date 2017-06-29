package com.peim

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.peim.http.HttpService
import com.peim.service.{AccountsService, CurrenciesService, UsersService}
import com.peim.utils.{Config, DatabaseService}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext

object Main extends App with Config with LazyLogging{

  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val databaseService = new DatabaseService
  val accountsService = new AccountsService(databaseService)
  val usersService = new UsersService(databaseService)
  val currenciesService = new CurrenciesService(databaseService)

  val routes = new HttpService(accountsService, usersService, currenciesService).routes

  Http().bindAndHandle(routes, httpHost, httpPort).map {
    binding => log.info("REST interface bound to {}", binding.localAddress)
  }.recover {
    case e: Exception =>
      log.error(e, s"REST interface could not bind to $httpHost:$httpPort")
      actorSystem.terminate()
  }
}

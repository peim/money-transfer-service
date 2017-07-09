package com.peim

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.peim.http.HttpService
import com.peim.repository._
import com.peim.service.TransfersService
import com.peim.utils.{BootData, Config, DatabaseService, ExceptionHandlers}
import scaldi.{Injectable, Module}

import scala.concurrent.duration._

object Main extends App with Injectable with Config with ExceptionHandlers {

  implicit val appModule = new Module {
    implicit val actorSystem = ActorSystem("money-transfer-service")
    implicit val ec = actorSystem.dispatcher

    bind[ActorSystem] to actorSystem destroyWith(_.terminate())
    bind[DatabaseService] to new DatabaseService()

    bind[AccountsRepository] to new AccountsRepository()
    bind[UsersRepository] to new UsersRepository()
    bind[CurrenciesRepository] to new CurrenciesRepository()
    bind[TransfersRepository] to new TransfersRepository()

    bind[TransfersService] to new TransfersService()
  }

  implicit val system = inject[ActorSystem]
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val log = Logging(system, getClass)

  new BootData().load()

  system.scheduler.schedule(10.seconds, 30.seconds, inject[TransfersService].rollbackFailedTransfers())

  val routes = new HttpService().routes

  Http().bindAndHandle(routes, httpHost, httpPort).map {
    binding => log.info("REST interface bound to {}", binding.localAddress)
  }.recover {
    case e: Exception =>
      log.error(e, s"REST interface could not bind to $httpHost:$httpPort")
      system.terminate()
  }
}

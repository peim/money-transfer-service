package com.peim

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.peim.http.HttpService
import com.peim.service.{AccountsService, CurrenciesService, UsersService}
import com.peim.utils.{BootData, Config, DatabaseService}
import scaldi.{Injectable, Module}

object Main extends App with Injectable with Config {

  implicit val appModule = new Module {
    implicit val actorSystem = ActorSystem("money-transfer-service")
    implicit val ec = actorSystem.dispatcher

    bind[ActorSystem] to actorSystem destroyWith(_.terminate())
    bind[DatabaseService] to new DatabaseService()
    bind[AccountsService] to new AccountsService()
    bind[UsersService] to new UsersService()
    bind[CurrenciesService] to new CurrenciesService()
  }

  implicit val system = inject[ActorSystem]
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val log = Logging(system, getClass)

  new BootData().load()

  val routes = new HttpService().routes

  Http().bindAndHandle(routes, httpHost, httpPort).map {
    binding => log.info("REST interface bound to {}", binding.localAddress)
  }.recover {
    case e: Exception =>
      log.error(e, s"REST interface could not bind to $httpHost:$httpPort")
      system.terminate()
  }
}

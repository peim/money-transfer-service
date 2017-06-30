package com.peim.http.api

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import com.peim.model.User
import com.peim.service.UsersService
import com.peim.utils.BootData
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.Json

class UsersServiceApiSpec extends BaseServiceTest with ScalaFutures {

  private val users = BootData.getUsers
  private val usersService = inject[UsersService]
  private val route = httpService.usersRouter

  "Users service" should {

    "retrieve users list" in {
      Get("/users") ~> route ~> check {
        status should be(OK)
        responseAs[Seq[User]] should have size 3
        responseAs[Seq[User]] should be(users)
      }
    }

    "retrieve user by id" in {
      val user = users.find(_.id == 1).get
      Get(s"/users/${user.id}") ~> route ~> check {
        responseAs[User] should be(user)
      }
    }

    "create user" in {
      val newUser = User(4, "John")
      val requestEntity = HttpEntity(MediaTypes.`application/json`, Json.toJson(newUser).toString)
      Post(s"/users", requestEntity) ~> route ~> check {
        status should be(Created)
        responseAs[Int] should be(newUser.id)
        whenReady(usersService.findById(newUser.id)) { result =>
          result should be(Some(newUser))
        }
      }
    }

    "update user" in {
      val user = users.find(_.id == 1).get
      val newUser = user.setName("John")
      val requestEntity = HttpEntity(MediaTypes.`application/json`, Json.toJson(newUser).toString)
      Put(s"/users/${newUser.id}", requestEntity) ~> route ~> check {
        status should be(NoContent)
        whenReady(usersService.findById(newUser.id)) { result =>
          result should be(Some(newUser))
        }
      }
    }

    "delete user" in {
      val user = users.find(_.id == 3).get
      Delete(s"/users/${user.id}") ~> route ~> check {
        response.status should be(NoContent)
        whenReady(usersService.findById(user.id)) { result =>
          result should be(Option.empty[User])
        }
      }
    }
  }
}

package com.peim.http.api

import com.peim.model.User
import org.scalatest.concurrent.ScalaFutures
import akka.http.scaladsl.model.StatusCodes._
import com.peim.utils.BootData

class UsersServiceApiSpec extends BaseServiceTest with ScalaFutures {

  val route = httpService.usersRouter

  "Users service" should {

    "retrieve users list" in {
      Get("/users") ~> route ~> check {
        status should be(OK)
        responseAs[Seq[User]] should have size 3
        responseAs[Seq[User]] should be(BootData.getUsers)
      }
    }

    //    "retrieve user by id" in new Context {
    //      val testUser = testUsers(4)
    //      Get(s"/users/${testUser.id.get}") ~> route ~> check {
    //        responseAs[UserEntity] should be(testUser)
    //      }
    //    }
    //
    //    "update user by id and retrieve it" in new Context {
    //      val testUser = testUsers(3)
    //      val newUsername = Random.nextString(10)
    //      val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"username": "$newUsername"}""")
    //
    //      Post(s"/users/${testUser.id.get}", requestEntity) ~> route ~> check {
    //        responseAs[UserEntity] should be(testUser.copy(username = newUsername))
    //        whenReady(getUserById(testUser.id.get)) { result =>
    //          result.get.username should be(newUsername)
    //        }
    //      }
    //    }
    //
    //    "delete user" in new Context {
    //      val testUser = testUsers(2)
    //      Delete(s"/users/${testUser.id.get}") ~> route ~> check {
    //        response.status should be(NoContent)
    //        whenReady(getUserById(testUser.id.get)) { result =>
    //          result should be(None: Option[UserEntity])
    //        }
    //      }
    //    }
    //
    //    "retrieve currently logged user" in new Context {
    //      val testUser = testUsers(1)
    //      val header = "Token" -> testTokens.find(_.userId.contains(testUser.id.get)).get.token
    //
    //      Get("/users/me") ~> addHeader(header._1, header._2) ~> route ~> check {
    //        responseAs[UserEntity] should be(testUsers.find(_.id.contains(testUser.id.get)).get)
    //      }
    //    }
    //
    //    "update currently logged user" in new Context {
    //      val testUser = testUsers.head
    //      val newUsername = Random.nextString(10)
    //      val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"username": "$newUsername"}""")
    //      val header = "Token" -> testTokens.find(_.userId.contains(testUser.id.get)).get.token
    //
    //      Post("/users/me", requestEntity) ~> addHeader(header._1, header._2) ~> route ~> check {
    //        responseAs[UserEntity] should be(testUsers.find(_.id.contains(testUser.id.get)).get.copy(username = newUsername))
    //        whenReady(getUserById(testUser.id.get)) { result =>
    //          result.get.username should be(newUsername)
    //        }
    //      }
    //    }

  }
}

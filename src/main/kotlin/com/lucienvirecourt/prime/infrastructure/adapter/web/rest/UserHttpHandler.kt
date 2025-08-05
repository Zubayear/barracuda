package com.lucienvirecourt.prime.infrastructure.adapter.web.rest

import com.lucienvirecourt.prime.application.usecase.user.CreateUser
import com.lucienvirecourt.prime.application.usecase.user.CreateUserOutcome
import com.lucienvirecourt.prime.infrastructure.adapter.web.rest.dto.CreateUserRequest
import com.lucienvirecourt.prime.infrastructure.adapter.web.rest.dto.CreateUserResponse
import io.vertx.core.json.Json
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserHttpHandler(private val createUser: CreateUser) {
  fun registerRoutes(router: Router) {
    router.post("/users").coroutineHandler { context -> createUserHandler(context) }
  }

  private suspend fun createUserHandler(context: RoutingContext) {
    // map request(http) to domain object
    // Utilize use case
    val requestBody = context.body()
      .asPojo(CreateUserRequest::class.java)

    val outcome = createUser.execute(requestBody.toDomain())
    when (outcome) {
      is CreateUserOutcome.Success -> {
        val response = CreateUserResponse.fromDomain(outcome.user)
        context.response()
          .setStatusCode(201)
          .putHeader("content-type", "application/json")
          .end(Json.encode(response))
      }

      is CreateUserOutcome.UserAlreadyExists -> {
        context.response()
          .setStatusCode(409) // Conflict
          .end("User with ID ${outcome.userId} already exists.")
      }

      is CreateUserOutcome.Failure -> {
        context.response()
          .setStatusCode(500) // Internal Server Error
          .end("An unexpected error occurred.")
      }

      is CreateUserOutcome.InvalidEmail -> {
        context.response()
          .setStatusCode(400) // Bad Request
          .end(outcome.message)
      }
    }
  }
}

fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit): Route {
  handler { ctx ->
    CoroutineScope(ctx.vertx().dispatcher()).launch {
      try {
        fn(ctx)
      } catch (e: Exception) {
        ctx.fail(e)
      }
    }
  }
  return this
}

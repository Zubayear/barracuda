package com.lucienvirecourt.prime.infrastructure.adapter.web.rest

import com.lucienvirecourt.prime.application.usecase.user.CreateUser
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserHttpHandler(private val createUser: CreateUser) {
  fun registerRoutes(router: Router) {
    router.post("/users").coroutineHandler { context -> createUserHandler(context)}
  }

  private suspend fun createUserHandler(context: RoutingContext) {
    // map request(http) to domain object
    // Utilize use case
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

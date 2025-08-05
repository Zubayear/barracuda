package com.lucienvirecourt.prime.infrastructure.verticle

import com.lucienvirecourt.prime.infrastructure.PgClientProvider
import com.lucienvirecourt.prime.application.usecase.user.CreateUser
import com.lucienvirecourt.prime.infrastructure.adapter.logging.Slf4jLoggerAdapter
import com.lucienvirecourt.prime.infrastructure.adapter.persistence.UserRepositoryAdapter
import com.lucienvirecourt.prime.infrastructure.adapter.web.rest.UserHttpHandler
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle

class WebServerVerticle : CoroutineVerticle() {
  override suspend fun start() {
    val dbClient = PgClientProvider.pool
    val userLogger = Slf4jLoggerAdapter(CreateUser::class.java.name)
    val userRepository = UserRepositoryAdapter(dbClient)
    val createUser = CreateUser(userRepository, userLogger)

    val userHandler = UserHttpHandler(createUser)
    val router = Router.router(vertx)
    router.route().handler(BodyHandler.create())
    userHandler.registerRoutes(router)

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080)
    super.start()
  }
}

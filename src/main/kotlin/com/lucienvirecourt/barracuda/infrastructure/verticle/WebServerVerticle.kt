package com.lucienvirecourt.barracuda.infrastructure.verticle

import com.lucienvirecourt.barracuda.infrastructure.PgClientProvider
import com.lucienvirecourt.barracuda.application.usecase.user.CreateUser
import com.lucienvirecourt.barracuda.infrastructure.adapter.logging.Slf4jLoggerAdapter
import com.lucienvirecourt.barracuda.infrastructure.adapter.persistence.UserRepositoryAdapter
import com.lucienvirecourt.barracuda.infrastructure.adapter.web.rest.UserHttpHandler
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

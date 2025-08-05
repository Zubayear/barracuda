package com.lucienvirecourt.prime.startup

import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.lucienvirecourt.prime.infrastructure.PgClientProvider
import com.lucienvirecourt.prime.infrastructure.verticle.WebServerVerticle
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait

class AppLauncher : CoroutineVerticle() {
  override suspend fun start() {
    setupJackson()
    PgClientProvider.init(vertx)
    vertx.deployVerticle(WebServerVerticle::class.java.name).coAwait()
    super.start()
  }

  fun setupJackson() {
    DatabindCodec.mapper().registerModule(kotlinModule())
  }
}

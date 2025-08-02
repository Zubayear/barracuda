package com.lucienvirecourt.prime.startup

import com.lucienvirecourt.prime.PgClientProvider
import com.lucienvirecourt.prime.infrastructure.verticle.WebServerVerticle
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait

class AppLauncher : CoroutineVerticle() {
  override suspend fun start() {
    PgClientProvider.init(vertx)
    vertx.deployVerticle(WebServerVerticle::class.java.name).coAwait()
    super.start()
  }
}

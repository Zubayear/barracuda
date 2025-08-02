package com.lucienvirecourt.prime

import io.vertx.core.Vertx
import io.vertx.pgclient.PgConnectOptions
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions

object PgClientProvider {
  lateinit var pool: Pool
    private set

  fun init(vertx: Vertx) {
    val connectionOptions =  PgConnectOptions().apply {
      port = 5432
      host = "localhost"
      database = "mfs_db"
      user = "postgres"
      password = "password"
    }

    val poolOptions = PoolOptions().apply {
      maxSize = 10
    }

    pool = Pool.pool(vertx, connectionOptions, poolOptions)
  }
}

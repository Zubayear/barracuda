package com.lucienvirecourt.barracuda.infrastructure.adapter.persistence

import com.lucienvirecourt.barracuda.application.port.out.UserRepositoryPort
import com.lucienvirecourt.barracuda.domain.model.user.User
import io.vertx.sqlclient.Pool

class UserRepositoryAdapter(private val client: Pool) : UserRepositoryPort {
  override suspend fun findById(id: Long): User? {
    TODO("Not yet implemented")
  }

  override suspend fun save(user: User): User {
    TODO("Not yet implemented")
  }
}

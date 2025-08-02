package com.lucienvirecourt.prime.application.port.out

import com.lucienvirecourt.prime.domain.model.user.User

// This is an Outer port or a Gateway. It's an interface that defines how the Use Case will interact with a data source
// This is an interface defined in the inner layer, to be implemented in an outer layer.
interface UserRepositoryPort {
  suspend fun findById(id: Long): User?
  suspend fun save(user: User): User
}

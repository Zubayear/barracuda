package com.lucienvirecourt.prime.infrastructure.adapter.web.rest.dto

import com.lucienvirecourt.prime.domain.model.user.User

data class CreateUserRequest(
  val id: Long? = null,
  val name: String,
  val email: String
) {
  fun toDomain(): User = User(id, name, email)
}

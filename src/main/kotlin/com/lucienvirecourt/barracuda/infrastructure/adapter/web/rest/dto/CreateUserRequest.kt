package com.lucienvirecourt.barracuda.infrastructure.adapter.web.rest.dto

import com.lucienvirecourt.barracuda.domain.model.user.User

data class CreateUserRequest(
  val id: Long? = null,
  val name: String,
  val email: String
) {
  fun toDomain(): User = User(id, name, email)
}

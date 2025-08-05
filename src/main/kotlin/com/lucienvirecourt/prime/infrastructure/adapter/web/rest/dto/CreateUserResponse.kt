package com.lucienvirecourt.prime.infrastructure.adapter.web.rest.dto

import com.lucienvirecourt.prime.domain.model.user.User

data class CreateUserResponse(val id: Long, val name: String, val email: String) {
  companion object {
    fun fromDomain(user: User): CreateUserResponse {
      return CreateUserResponse(user.id!!, user.name, user.email)
    }
  }
}

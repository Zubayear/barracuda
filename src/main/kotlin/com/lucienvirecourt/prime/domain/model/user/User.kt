package com.lucienvirecourt.prime.domain.model.user

// This is our Entity. It's a simple data class that represents a core business concept.
data class User(
  val id: Long?,
  val name: String,
  val email: String
) {
  fun isEmailValid(): Boolean = email.isNotBlank()
  fun isNameValid(): Boolean = name.isNotBlank()
}

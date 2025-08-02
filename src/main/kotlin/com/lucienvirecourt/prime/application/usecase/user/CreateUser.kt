package com.lucienvirecourt.prime.application.usecase.user

import com.lucienvirecourt.prime.application.port.out.UserRepositoryPort
import com.lucienvirecourt.prime.domain.model.user.User

// This is a Use Case class. It implements a specific business action
// It depends on the UserRepositoryPort interface from the inner layer, but it doesn't know about its concrete implementation
class CreateUser(private val userRepository: UserRepositoryPort) {
  suspend fun execute(user: User): User {
    // Here's where your application business rules would go
    // For example, validating the email format, checking for duplicates, etc.
    val newUser = User(
      id = 12,
      name = user.name,
      email = user.email
    )
    return userRepository.save(newUser)
  }
}

package com.lucienvirecourt.prime.application.usecase.user

import com.lucienvirecourt.prime.application.port.out.LoggerPort
import com.lucienvirecourt.prime.application.port.out.UserRepositoryPort
import com.lucienvirecourt.prime.domain.model.user.User

sealed class CreateUserOutcome {
  data class Success(val user: User) : CreateUserOutcome()
  data class InvalidEmail(val message: String) : CreateUserOutcome()
  data class UserAlreadyExists(val userId: Long) : CreateUserOutcome()
  object Failure : CreateUserOutcome()
}

// This is a Use Case class. It implements a specific business action
// It depends on the UserRepositoryPort interface from the inner layer, but it doesn't know about its concrete implementation
class CreateUser(
  private val userRepository: UserRepositoryPort,
  private val logger: LoggerPort
) {
  suspend fun execute(user: User): CreateUserOutcome {
    logger.info("Attempting to create a new user with ${user.email}")
    if (!user.isEmailValid()) return CreateUserOutcome.InvalidEmail("The provided email is not valid.")
    // Here's where your application business rules would go
    // For example, validating the email format, checking for duplicates, etc.
    val newUser = User(
      id = 12,
      name = user.name,
      email = user.email
    )
    return CreateUserOutcome.Success(userRepository.save(newUser))
  }
}

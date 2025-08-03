# prime

# Clean Architecture Template with Vert.x and Kotlin

This is a robust and scalable project template demonstrating Clean Architecture principles with a non-blocking, reactive approach using Vert.x, Kotlin, and Coroutines. It provides a solid foundation for building maintainable, testable, and framework-independent microservices and APIs.

## 🚀 Features
* Architecture: Adheres strictly to Clean Architecture's layered structure (Domain, Application, Infrastructure).

* Framework: Vert.x for a high-performance, non-blocking, event-driven web server.

* Language: Kotlin with Coroutines for writing asynchronous code that is as readable as synchronous code.

* Build Tool: Maven.

* Testing: JUnit 5 for unit and integration testing.

* Dependency Management: Manual Dependency Injection for clarity and simplicity.

* Code Quality: Sensible default configurations for a clean and maintainable codebase.

## 🏗️ Project Structure Explained
The project is organized into layers, following the Dependency Rule of Clean Architecture: dependencies can only point inwards.

```aiignore
├── src
│   ├── main
│   │   ├── kotlin
│   │   │   ├── com/yourcompany/yourproject
│   │   │   │   ├── domain         <- The Core Business Logic (Entities)
│   │   │   │   │   └── model
│   │   │   │   │       └── user
│   │   │   │   │           └── User.kt
│   │   │   │   ├── application    <- The Application Business Rules (Use Cases & Ports)
│   │   │   │   │   ├── usecase
│   │   │   │   │   │   └── user
│   │   │   │   │   │       └── CreateUser.kt
│   │   │   │   │   └── port
│   │   │   │   │       ├── out
│   │   │   │   │       │   └── UserRepositoryPort.kt
│   │   │   │   ├── infrastructure <- The Outer Layer (Adapters, Frameworks, Verticles)
│   │   │   │   │   ├── adapter
│   │   │   │   │   │   ├── persistence
│   │   │   │   │   │   │   └── UserRepositoryAdapter.kt
│   │   │   │   │   │   └── web
│   │   │   │   │   │       └── rest
│   │   │   │   │   │           └── UserHttpHandler.kt
│   │   │   │   │   └── verticle
│   │   │   │   │       └── WebServerVerticle.kt
│   │   │   │   └── startup        <- The Entry Point
│   │   │   │       └── AppLauncher.kt
```    

1. `domain`

    The Innermost Layer. This is the heart of your application. It contains pure Kotlin data structures (`Entities`) and business-wide rules.

    * `model`: Contains simple data classes like `User.kt` that represent core business concepts.

    * Key Rule: This layer has no dependencies on any other layer or external framework.
2. `application`
   
    The Use Case Layer. This layer orchestrates the flow of data to and from the entities. It defines the application's specific behavior and the contracts for interacting with the outside world.

    * `usecase`: Contains `Use Case` classes (e.g., `CreateUser.kt`) that implement a specific business action.

    * `port/out`: Contains `Output Ports`. These are interfaces (e.g., `UserRepositoryPort.kt`) that define how a Use Case will communicate with an external system (like a database).

3. `infrastructure`

   The Outermost Layer. This is the "dirty" layer where all external concerns live. It contains the concrete implementations that make the application run.

    * `adapter/persistence`: Contains `Repository Adapters` (e.g., `UserRepositoryAdapter.kt`) that implement the `Output Port` interfaces using a specific database technology (e.g., a Vert.x PostgreSQL client).

    * `adapter/web/rest`: Contains `Web Handlers/Controllers` (e.g., `UserHttpHandler.kt`) that handle incoming HTTP requests and use the Use Cases to perform actions.

    * `verticle`: Contains the Vert.x `Verticles` that manage the lifecycle of services, such as the `WebServerVerticle` which sets up the HTTP server and wires up the dependencies.

4. `startup`
   The Application Entry Point. A simple class with a main function to launch and deploy the Verticles. This is where the entire application starts.

## 🔧 Getting Started
#### Prerequisites
    JDK 21 or higher 
    Maven (typically comes bundled with most IDEs)

Clone the Repository
```aiignore
git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name
```

To launch your tests:
```aiignore
./mvnw clean test
```

To package your application:
```aiignore
./mvnw clean package
```

To run your application:
```aiignore
./mvnw clean compile exec:java
```

## 🧑‍💻 How to Use the Template
This section provides a step-by-step guide to adding a new feature.

Scenario: Let's assume we want to add a feature to retrieve a User by their id.

Step 1: Define the Use Case
Create a new Use Case class in `src/main/kotlin/.../application/usecase/user/GetUser.kt`.

```aiignore
// GetUser.kt
package com.yourcompany.yourproject.application.usecase.user

import com.yourcompany.yourproject.application.port.out.UserRepositoryPort
import com.yourcompany.yourproject.domain.model.user.User

class GetUser(private val userRepository: UserRepositoryPort) {
    suspend fun execute(id: String): User? {
        return userRepository.findById(id)
    }
}
```

Step 2: Update the Port Interface
Add the findById method to the UserRepositoryPort interface in `src/main/kotlin/.../application/port/out/UserRepositoryPort.kt`.

```aiignore
// UserRepositoryPort.kt
package com.yourcompany.yourproject.application.port.out

import com.yourcompany.yourproject.domain.model.user.User

interface UserRepositoryPort {
    suspend fun findById(id: String): User?
    //... other methods
}
```

Step 3: Implement the Adapter
Implement the new method in the UserRepositoryAdapter in `src/main/kotlin/.../infrastructure/adapter/persistence/UserRepositoryAdapter.kt`.
```aiignore
// UserRepositoryAdapter.kt
package com.yourcompany.yourproject.infrastructure.adapter.persistence

import com.yourcompany.yourproject.application.port.out.UserRepositoryPort
import com.yourcompany.yourproject.domain.model.user.User
//... other imports

class UserRepositoryAdapter(/*... dependencies ...*/) : UserRepositoryPort {
    override suspend fun findById(id: String): User? {
        // Concrete database query logic goes here
        // e.g., using a Vert.x database client
        // val result = dbClient.query("SELECT * FROM users WHERE id = '$id'").execute().await()
        return null // Placeholder
    }
    //... other methods
}
```

Step 4: Update the Web Handler
Add a new route and a handler to `src/main/kotlin/.../infrastructure/adapter/web/rest/UserHttpHandler.kt`.
```aiignore
// UserHttpHandler.kt
//... imports
import com.yourcompany.yourproject.application.usecase.user.GetUser

class UserHttpHandler(private val createUser: CreateUser, private val getUser: GetUser) {
    fun registerRoutes(router: Router) {
        router.post("/users").coroutineHandler { context -> createUserHandler(context)}
        router.get("/users/:id").coroutineHandler { context -> getUserHandler(context)}
    }

    private suspend fun getUserHandler(context: RoutingContext) {
        val userId = context.pathParam("id")
        val user = getUser.execute(userId)
        if (user != null) {
            context.response()
                .setStatusCode(200)
                .putHeader("content-type", "application/json")
                .end(user.toJson()) // assuming you have a toJson() method
        } else {
            context.response().setStatusCode(404).end()
        }
    }
    //... other handlers
}
```

Step 5: Wire Dependencies in the Verticle
Finally, add the new Use Case to the dependency injection section of your `WebServerVerticle` in `src/main/kotlin/.../infrastructure/verticle/WebServerVerticle.kt`.

```aiignore
// WebServerVerticle.kt
//... imports
import com.yourcompany.yourproject.application.usecase.user.GetUser

class WebServerVerticle : CoroutineVerticle() {
    override suspend fun start() {
        val dbClient = //... create your database client
        val userRepository = UserRepositoryAdapter(dbClient)

        // Instantiate the Use Cases
        val createUser = CreateUser(userRepository)
        val getUser = GetUser(userRepository)

        // Instantiate the handler with its dependencies
        val userHttpHandler = UserHttpHandler(createUser, getUser)

        val router = Router.router(vertx)
        userHttpHandler.registerRoutes(router)

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8080)
    }
}
```

## 🤝 Contributing
We welcome contributions! If you have suggestions or find a bug, please feel free to open an issue or submit a pull request. This template is a starting point, and your feedback is highly valued.

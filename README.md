# TaskEngine // RESTful Task & Issue Tracking API

A robust, production-ready Task and Issue Tracking REST API built with **Java 21 LTS**, **Spring Boot 3.3**, **Clean Layered Architecture**, **Jakarta Bean Validation**, and **RFC 7807 ProblemDetail** error handling.

---

##  Features

- ** Clean Layered Architecture (`controller` → `service` → `model` / `dto`):**
  - Strict separation of concerns keeping presentation, validation, business rules, and domain models completely decoupled.
  - **Constructor Injection**: All dependencies injected through constructor parameters for immutability and testability without reflection.

- ** Immutable Java 21 Records & DTO Pattern (`dto/`):**
  - **Information Hiding**: `CreateTaskRequest`, `UpdateTaskRequest`, and `TaskResponse` isolate internal domain storage from public API wire contracts.
  - **Static Factory Mapper (`TaskResponse.from(Task)`):** Encapsulates model-to-DTO transformation inside the record itself.
  - **Shallow Immutability**: Auto-generated accessors, equals, hashCode, and toString with zero boilerplate.

- ** High-Throughput Thread-Safe In-Memory Store (`service/TaskService.java`):**
  - **`ConcurrentHashMap<UUID, Task>`**: High-concurrency in-memory storage supporting non-blocking concurrent reads and atomic writes.
  - **Immutable State Swaps**: Updates create new immutable `Task` records preserving original `id` and `createdAt` timestamps with zero race conditions.

- ** RESTful HTTP Semantics & Location Headers (`controller/TaskController.java`):**
  - `POST /api/tasks` → Returns **`201 Created`** with standard `Location: /api/tasks/{id}` header.
  - `GET /api/tasks` → Returns **`200 OK`** with collection list.
  - `GET /api/tasks/{id}` → Returns **`200 OK`** with single resource.
  - `PUT /api/tasks/{id}` → Returns **`200 OK`** with updated resource.
  - `DELETE /api/tasks/{id}` → Returns **`204 No Content`** with empty response body.

- ** Jakarta Bean Validation (`@Valid`):**
  - Runtime validation constraints on incoming payloads (`@NotBlank`, `@Size(max = 100)`, `@NotNull`).
  - Guards business logic from bad data, blank strings, and null enums before execution.

- ** Centralized RFC 7807 Error Handling (`exception/GlobalExceptionHandler.java`):**
  - **Global Interceptor (`@RestControllerAdvice`)**: Intercepts unhandled domain exceptions across all controllers.
  - **Standardized `ProblemDetail` JSON**: Emits IETF RFC 7807 compliant error responses for `404 Not Found` and `400 Bad Request`.
  - **Field-Level Validation Error Maps**: Collects all failed fields into a structured `errors` map for frontend consumption.

- ** Comprehensive Test Suite (16 Passing Tests):**
  - **Unit Tests (`TaskServiceTest.java`)**: 8 isolated, sub-second business logic tests using **JUnit 5** and fluent **AssertJ** assertions.
  - **Integration Slice Tests (`TaskControllerTest.java`)**: 7 lightweight HTTP tests using **MockMvc** (`@WebMvcTest`) verifying routing, JSONPath payloads, HTTP headers, and RFC 7807 error structures.
  - **Context Test (`TaskEngineApplicationTests.java`)**: Verifies Spring Boot ApplicationContext bootstrapping and bean wiring.

---

##  Tech Stack

- **Framework:** [Spring Boot 3.3.5](https://spring.io/projects/spring-boot)
- **Language:** [Java 21 LTS](https://openjdk.org/projects/jdk/21/)
- **Build Tool:** [Maven Wrapper (mvnw)](https://maven.apache.org/)
- **Validation:** [Jakarta Bean Validation](https://beanvalidation.org/) (Hibernate Validator)
- **Testing:** [JUnit 5](https://junit.org/junit5/), [AssertJ](https://assertj.github.io/doc/), [MockMvc](https://docs.spring.io/spring-framework/reference/testing/spring-mvc-test-framework.html), [Mockito](https://site.mockito.org/)
- **Error Standard:** [RFC 7807 Problem Details](https://datatracker.ietf.org/doc/html/rfc7807)

---

##  Project Architecture

```text
src/
├── main/
│   └── java/com/devpulse/taskengine/
│       ├── TaskEngineApplication.java            # Spring Boot main application entrypoint
│       ├── controller/
│       │   └── TaskController.java               # REST controller with 5 HTTP endpoints
│       ├── dto/
│       │   ├── CreateTaskRequest.java            # Validated record for incoming POST payloads
│       │   ├── UpdateTaskRequest.java            # Validated record for incoming PUT payloads
│       │   └── TaskResponse.java                 # Public output record with static factory mapper
│       ├── exception/
│       │   ├── GlobalExceptionHandler.java       # @RestControllerAdvice with RFC 7807 ProblemDetail
│       │   └── TaskNotFoundException.java        # Custom domain unchecked exception
│       ├── model/
│       │   ├── Priority.java                     # Enum (LOW, MEDIUM, HIGH)
│       │   ├── Task.java                         # Immutable domain entity record (UUID, Instant)
│       │   └── TaskStatus.java                   # Enum (TODO, IN_PROGRESS, COMPLETED)
│       └── service/
│           └── TaskService.java                  # Business logic & ConcurrentHashMap store
└── test/
    └── java/com/devpulse/taskengine/
        ├── TaskEngineApplicationTests.java       # ApplicationContext boot verification test
        ├── controller/
        │   └── TaskControllerTest.java           # MockMvc slice tests (Endpoints, 400, 404)
        └── service/
            └── TaskServiceTest.java              # Unit tests with JUnit 5 & AssertJ
```

---

##  REST API Endpoints

Base URL: `http://localhost:8080/api/tasks`

| Method | Endpoint | Description | Status Code | Response |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/tasks` | Create a new task | `201 Created` | `TaskResponse` + `Location` Header |
| `GET` | `/api/tasks` | Retrieve all tasks | `200 OK` | `List<TaskResponse>` |
| `GET` | `/api/tasks/{id}` | Retrieve task by UUID | `200 OK` | `TaskResponse` (or `404 ProblemDetail`) |
| `PUT` | `/api/tasks/{id}` | Update task by UUID | `200 OK` | `TaskResponse` (or `404 ProblemDetail`) |
| `DELETE` | `/api/tasks/{id}` | Delete task by UUID | `204 No Content` | Empty Body (or `404 ProblemDetail`) |

---

##  Getting Started

### Prerequisites
Make sure you have **Java JDK 21+** and **Git** installed on your machine.

### Installation & Running

1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/task-engine-api.git
   cd task-engine-api
   ```

2. Run the full automated test suite:
   ```bash
   # Windows PowerShell
   .\mvnw.cmd test

   # macOS / Linux
   ./mvnw test
   ```

3. Start the development server:
   ```bash
   # Windows PowerShell
   .\mvnw.cmd spring-boot:run

   # macOS / Linux
   ./mvnw spring-boot:run
   ```

4. The API will be live and ready for requests at:
   ```text
   http://localhost:8080/api/tasks
   ```

5. Build production executable JAR:
   ```bash
   # Windows PowerShell
   .\mvnw.cmd clean package
   java -jar target/task-engine-0.0.1-SNAPSHOT.jar
   ```

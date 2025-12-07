# Devices API

A production-ready REST API for managing devices, built with **Quarkus 3.x**, **Java 21**, following **Domain-Driven Design (DDD)** and **Test-Driven Development (TDD)** principles.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Docker Deployment](#docker-deployment)
- [Project Structure](#project-structure)

## 🎯 Overview

This API provides comprehensive device management capabilities with the following functionalities:

- ✅ Create a new device
- ✅ Fully and/or partially update an existing device
- ✅ Fetch a single device
- ✅ Fetch all devices
- ✅ Fetch devices by brand
- ✅ Fetch devices by state
- ✅ Delete a single device

### Business Rules

- **Creation time** cannot be updated
- **Name and brand** properties cannot be updated if the device is in use
- **In-use devices** cannot be deleted

## 🏗️ Architecture

This project follows **Domain-Driven Design (DDD)** principles with a clean architecture:

```
com.test.devices/
├── domain/                    # Pure business logic (NO external dependencies)
│   ├── model/                 # Aggregates, Entities, Value Objects
│   │   ├── Device.java        # Device aggregate root
│   │   └── DeviceState.java   # Value object for device state
│   └── repository/            # Repository interface
│       └── DeviceRepository.java
│
├── application/               # Use cases + DTOs (orchestration)
│   ├── dto/                   # Data Transfer Objects
│   │   ├── CreateDeviceRequest.java
│   │   ├── UpdateDeviceRequest.java
│   │   └── DeviceResponse.java
│   ├── exception/             # Application exceptions
│   │   ├── DeviceNotFoundException.java
│   │   └── DeviceValidationException.java
│   └── usecase/               # Business use cases
│       ├── CreateDeviceUseCase.java
│       ├── UpdateDeviceUseCase.java
│       ├── GetDeviceUseCase.java
│       ├── ListDevicesUseCase.java
│       └── DeleteDeviceUseCase.java
│
├── infrastructure/            # External systems, databases
│   └── persistance/           # JPA implementation
│       ├── DeviceEntity.java
│       └── JpaDeviceRepository.java
│
└── presentation/              # REST endpoints
    ├── rest/                  # HTTP/REST controllers
    │   └── DeviceResource.java
    ├── exception/             # Exception mappers
    │   ├── ErrorResponse.java
    │   ├── DeviceNotFoundExceptionMapper.java
    │   ├── DeviceValidationExceptionMapper.java
    │   └── GenericExceptionMapper.java
    └── health/                # Health checks
        ├── LivenessHealthCheck.java
        └── ReadinessHealthCheck.java
```

### Dependency Flow

```
Presentation → Application → Domain ← Infrastructure
                                ↑
                         PURE BUSINESS LOGIC
```

## ✨ Features

### Device Properties

- **ID**: Auto-generated unique identifier
- **Name**: Device name (required)
- **Brand**: Device brand (required)
- **State**: Device state (AVAILABLE, IN_USE, INACTIVE)
- **Creation Time**: Automatically set and immutable

### API Capabilities

- RESTful API design
- OpenAPI/Swagger documentation
- Comprehensive input validation
- Proper error handling with meaningful messages (4xx and 5xx)
- Health check endpoints (liveness and readiness)
- PostgreSQL persistence
- Containerized deployment

## 🛠 Technology Stack

- **Framework**: Quarkus 3.6.4
- **Language**: Java 21
- **Build Tool**: Gradle 8.14
- **Database**: PostgreSQL 16 (H2 for dev/test)
- **ORM**: Hibernate ORM with JPA
- **Testing**: JUnit 5, Mockito, REST Assured, AssertJ
- **API Documentation**: OpenAPI 3.0 (Swagger UI)
- **Containerization**: Docker & Docker Compose

## 🚀 Getting Started

### Prerequisites

- Java 21 (install via SDKMAN)
- Gradle 8+ (wrapper included)
- Docker & Docker Compose (for containerized deployment)
- PostgreSQL 16 (if running locally without Docker)

### Install Java 21 using SDKMAN

```bash
sdk install java 21.0.1-tem
sdk use java 21.0.1-tem
```

### Run Locally (Development Mode)

1. **Start PostgreSQL** (using Docker):

```bash
docker run --name postgres-dev -e POSTGRES_DB=devicesdb \
  -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 -d postgres:16-alpine
```

2. **Run the application in dev mode**:

```bash
./gradlew quarkusDev
```

The application will start on `http://localhost:8080` with hot reload enabled.

### Build the Application

```bash
./gradlew clean build
```

### Run Tests

```bash
# Run all tests
./gradlew test

# Run with coverage report
./gradlew test jacocoTestReport
```

## 📚 API Documentation

### Endpoints

#### Create Device
```http
POST /api/v1/devices
Content-Type: application/json

{
  "name": "iPhone 15",
  "brand": "Apple",
  "state": "AVAILABLE"
}
```

#### Update Device
```http
PUT /api/v1/devices/{id}
Content-Type: application/json

{
  "name": "iPhone 15 Pro",  // Optional
  "brand": "Apple Inc",      // Optional
  "state": "IN_USE"          // Optional
}
```

#### Get Device by ID
```http
GET /api/v1/devices/{id}
```

#### List All Devices
```http
GET /api/v1/devices

# Filter by brand
GET /api/v1/devices?brand=Apple

# Filter by state
GET /api/v1/devices?state=AVAILABLE
```

#### Delete Device
```http
DELETE /api/v1/devices/{id}
```

#### Health Check
```http
GET /q/health

# Liveness check
GET /q/health/live

# Readiness check
GET /q/health/ready
```

### Swagger UI

Access the interactive API documentation at:
```
http://localhost:8080/swagger-ui
```

### OpenAPI Specification

Access the OpenAPI spec at:
```
http://localhost:8080/q/openapi
```

## 🧪 Testing

This project follows **Test-Driven Development (TDD)** principles with comprehensive test coverage:

### Test Layers

1. **Unit Tests**: Domain layer (pure business logic)
   - `DeviceTest.java` - Device aggregate tests
   - `CreateDeviceUseCaseTest.java` - Use case tests
   - `UpdateDeviceUseCaseTest.java`
   - `DeleteDeviceUseCaseTest.java`

2. **Integration Tests**: Full stack tests
   - `DeviceResourceTest.java` - REST API integration tests

### Run Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "DeviceResourceTest"

# Run tests with logging
./gradlew test --info

# Generate coverage report
./gradlew test jacocoTestReport
# Report at: build/reports/jacoco/test/html/index.html
```

### Test Coverage

The project maintains **>70% code coverage** as per best practices.

## 🐳 Docker Deployment

### Build and Run with Docker Compose

1. **Build the application**:

```bash
./gradlew clean build
```

2. **Start all services** (PostgreSQL + API):

```bash
docker-compose up -d
```

3. **Check logs**:

```bash
docker-compose logs -f devices-api
```

4. **Stop services**:

```bash
docker-compose down
```

### Manual Docker Build

```bash
# Build the application
./gradlew clean build

# Build Docker image
docker build -t devices-api:latest .

# Run the container
docker run -d -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=5432 \
  -e DB_NAME=devicesdb \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  --name devices-api \
  devices-api:latest
```

## 📁 Project Structure

```
devices-api/
├── src/
│   ├── main/
│   │   ├── java/com/test/devices/
│   │   │   ├── domain/          # Domain layer (pure business logic)
│   │   │   ├── application/     # Application layer (use cases)
│   │   │   ├── infrastructure/  # Infrastructure layer (persistence)
│   │   │   └── presentation/    # Presentation layer (REST)
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/test/devices/
│       │   ├── domain/model/
│       │   ├── application/usecase/
│       │   └── presentation/rest/
│       └── resources/
│           └── application.properties
├── build.gradle.kts
├── gradle.properties
├── Dockerfile
├── docker-compose.yml
└── README.md
```

## 🔧 Configuration

### Application Properties

Production configuration (`src/main/resources/application.properties`):

```properties
# Database
quarkus.datasource.db-kind=postgresql
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/devicesdb
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres

# Hibernate
quarkus.hibernate-orm.database.generation=update
quarkus.hibernate-orm.log.sql=true

# HTTP
quarkus.http.port=8080

# OpenAPI
quarkus.swagger-ui.always-include=true
quarkus.swagger-ui.path=/swagger-ui
```

### Environment Variables

Override configuration using environment variables:

- `DB_HOST`: Database host (default: localhost)
- `DB_PORT`: Database port (default: 5432)
- `DB_NAME`: Database name (default: devicesdb)
- `DB_USERNAME`: Database username (default: postgres)
- `DB_PASSWORD`: Database password (default: postgres)

## 🎯 Design Principles

### Domain-Driven Design (DDD)

- **Domain Layer**: Contains pure business logic with no external dependencies
- **Application Layer**: Orchestrates use cases and coordinates domain objects
- **Infrastructure Layer**: Implements technical details (database, external services)
- **Presentation Layer**: Handles HTTP requests and responses

### Test-Driven Development (TDD)

- Tests written before implementation
- Red-Green-Refactor cycle
- Comprehensive test coverage (unit, integration, API tests)

### Clean Code

- Single Responsibility Principle
- Meaningful names
- Small, focused methods
- Proper error handling
- Comprehensive documentation

## 🚧 Future Improvements

- [ ] Add authentication and authorization (JWT)
- [ ] Implement pagination for list endpoints
- [ ] Add caching layer (Redis)
- [ ] Implement soft delete functionality
- [ ] Add audit logging (who/when created/updated)
- [ ] Implement device history tracking
- [ ] Add batch operations (bulk create/update/delete)
- [ ] Implement event sourcing for device state changes
- [ ] Add GraphQL support
- [ ] Implement rate limiting
- [ ] Add monitoring and metrics (Prometheus/Grafana)
- [ ] Implement circuit breaker pattern
- [ ] Add database migration tool (Flyway/Liquibase)

## 📝 License


## 👤 Author

Built with ❤️ following DDD and TDD best practices.

# Copy the application
COPY --chown=185 build/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 build/quarkus-app/*.jar /deployments/
COPY --chown=185 build/quarkus-app/app/ /deployments/app/
COPY --chown=185 build/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]

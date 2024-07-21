# Xujie-Assignment

This is a Spring Boot application that demonstrates CRUD operations using MongoDB as the database. It includes basic member, product, and order management functionalities with proper error handling and global exception handling.

## Prerequisites

- Java 17
- MongoDB
- Gradle

## Setup

### Add MongoDB URI

Before running the application, you need to configure the MongoDB URI in the `application.properties` file located in `src/main/resources`.

```properties
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@cluster0.mongodb.net/<database>?retryWrites=true&w=majority
```

### Running

```
./gradlew build
./gradlew bootRun
```

## Swagger Integration

The application uses Swagger for API documentation. You can access the Swagger UI at http://localhost:8080/swagger-ui.html once the application is running.

```
http://localhost:8080/swagger-ui/index.html
```
# GitHub Copilot Instructions for Java Backend

## Project Context
This is a gRPC-only Spring Boot backend (Java 24) that serves as part of a full-stack gRPC-Web application. The backend communicates exclusively via gRPC protocol - **no REST endpoints**.

## Architecture Overview
- **Backend**: Spring Boot 3.5+ with Spring gRPC integration (Port 9090)
- **Frontend**: React TypeScript with gRPC-Web client (Port 3000)  
- **Proxy**: Envoy proxy for gRPC-Web translation (Port 8080)
- **Protocol**: 100% gRPC communication using Protocol Buffers

## Key Technologies & Dependencies
- Spring Boot 3.5.4-SNAPSHOT
- Spring gRPC 0.8.0 (`spring-grpc-server-spring-boot-starter`)
- gRPC 1.72.0 (`grpc-services`, `grpc-netty`)
- Protocol Buffers 4.30.2
- Maven build system with protobuf plugin

## Code Generation & Build Process
- **Proto Files**: Located in `backend/src/main/proto/`
- **Generated Code**: Auto-generated Java classes in `target/generated-sources/protobuf/`
- **Build**: Maven automatically regenerates Java classes from .proto files during compilation
- **Plugin**: Uses `io.github.ascopes:protobuf-maven-plugin` for code generation

## Spring gRPC Service Implementation Patterns

### Service Registration
Always use `@GrpcService` annotation for gRPC service implementations:
```java
@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {
    // Implementation methods
}
```

### Method Implementation Pattern
Follow this pattern for gRPC method implementations:
```java
@Override
public void getAllUsers(GetAllUsersRequest request, 
                       StreamObserver<GetAllUsersResponse> responseObserver) {
    try {
        // Business logic here
        GetAllUsersResponse response = GetAllUsersResponse.newBuilder()
            .addAllUsers(usersList)
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    } catch (Exception e) {
        responseObserver.onError(Status.INTERNAL
            .withDescription("Error message")
            .withCause(e)
            .asRuntimeException());
    }
}
```

### Error Handling
Use gRPC Status codes for proper error responses:
```java
// Not found
responseObserver.onError(Status.NOT_FOUND
    .withDescription("User not found")
    .asRuntimeException());

// Invalid argument
responseObserver.onError(Status.INVALID_ARGUMENT
    .withDescription("Invalid user data")
    .asRuntimeException());

// Internal error
responseObserver.onError(Status.INTERNAL
    .withDescription("Database error")
    .withCause(exception)
    .asRuntimeException());
```

## Configuration Requirements

### Application Properties
```properties
# gRPC server configuration
grpc.server.port=9090
grpc.server.reflection-service-enabled=true

# No web server needed (gRPC only)
# spring.main.web-application-type=none  # Keep commented to allow actuator
```

### Maven Dependencies
Essential dependencies for gRPC functionality:
```xml
<dependency>
    <groupId>org.springframework.grpc</groupId>
    <artifactId>spring-grpc-server-spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-services</artifactId>
</dependency>
```

## Protocol Buffer Guidelines

### Message Design
- Use clear, descriptive field names
- Include proper field numbering (never reuse numbers)
- Add validation annotations where appropriate
- Use appropriate data types (int64 for IDs, string for text, etc.)

### Service Definition
```protobuf
service UserService {
  rpc GetUser(GetUserRequest) returns (User);
  rpc GetAllUsers(GetAllUsersRequest) returns (GetAllUsersResponse);
  rpc CreateUser(CreateUserRequest) returns (User);
  rpc UpdateUser(UpdateUserRequest) returns (User);
  rpc DeleteUser(DeleteUserRequest) returns (DeleteUserResponse);
  rpc StreamUsers(StreamUsersRequest) returns (stream User);
}
```

## Development Workflow

### Making Proto Changes
1. Modify `.proto` files in `backend/src/main/proto/`
2. Run `mvn clean compile` to regenerate Java classes
3. Update service implementations to match new proto definitions
4. Coordinate with frontend team for client regeneration

### Testing gRPC Services
- Use gRPC reflection for service discovery
- Test with grpcurl: `grpcurl -plaintext localhost:9090 list`
- Implement proper unit tests for service methods
- Use gRPC testing utilities for integration tests

## Best Practices

### Code Organization
- Keep service implementations in `org.jrg.grpc.service` package
- Separate business logic from gRPC concerns
- Use dependency injection for service dependencies
- Implement proper logging with SLF4J

### Performance Considerations
- Use streaming for large datasets
- Implement connection pooling for database access
- Consider caching for frequently accessed data
- Monitor gRPC metrics via Spring Boot Actuator

### Security Implementation
- Add authentication interceptors for gRPC services
- Implement authorization checks in service methods
- Use TLS for production gRPC communication
- Validate all input parameters

## Common Patterns

### Builder Pattern for Responses
```java
User user = User.newBuilder()
    .setId(userId)
    .setName(userName)
    .setEmail(userEmail)
    .setRole(userRole)
    .setCreatedAt(timestamp)
    .build();
```

### Streaming Implementation
```java
@Override
public void streamUsers(StreamUsersRequest request, 
                       StreamObserver<User> responseObserver) {
    try {
        userRepository.findAll().forEach(user -> {
            responseObserver.onNext(convertToProto(user));
        });
        responseObserver.onCompleted();
    } catch (Exception e) {
        responseObserver.onError(e);
    }
}
```

## Troubleshooting Guidelines

### Common Issues
- **Port conflicts**: Ensure port 9090 is available
- **Proto compilation errors**: Check .proto syntax and field numbering
- **Service registration**: Verify `@GrpcService` annotation is present
- **Envoy connection**: Backend must be reachable from Docker container

### Debugging
- Enable gRPC debug logging in application.properties
- Use Spring Boot Actuator for health checks
- Monitor gRPC metrics and connection stats
- Check generated protobuf classes in target directory

## Integration Points
- **Frontend**: Communicates via Envoy proxy on port 8080
- **Database**: Integrate using Spring Data JPA or similar
- **Monitoring**: Use Spring Boot Actuator endpoints
- **Testing**: gRPC testing framework for unit/integration tests

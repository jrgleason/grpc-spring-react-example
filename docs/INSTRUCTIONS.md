# gRPC-Web Full-Stack Application Instructions

This document explains the architecture, components, and setup process for this gRPC-only full-stack application.

## Architecture Overview

This application demonstrates a modern gRPC-Web architecture with:
- **Backend**: Spring Boot with gRPC server (Java 24)
- **Frontend**: React with TypeScript using gRPC-Web
- **Proxy**: Envoy proxy for gRPC-Web translation
- **Protocol**: Only gRPC communication (no REST endpoints)

```
┌─────────────────┐    gRPC-Web     ┌─────────────────┐    gRPC    ┌─────────────────┐
│   React App     │◄──────────────► │   Envoy Proxy   │◄──────────►│  Spring Boot    │
│   (Port 3000)   │                 │   (Port 8080)   │            │   (Port 9090)   │
└─────────────────┘                 └─────────────────┘            └─────────────────┘
```

## Why Each Component is Needed

### 1. Spring Boot Backend (Port 9090)
**Purpose**: Serves as the gRPC server that handles business logic and data operations.

**Why gRPC instead of REST**:
- Type-safe communication with Protocol Buffers
- Better performance with HTTP/2
- Automatic code generation for multiple languages
- Built-in streaming capabilities
- Strong contract definition with .proto files

**Key Files**:
- `backend/src/main/proto/user_service.proto` - Service definition
- `backend/src/main/java/org/jrg/grpc/service/UserGrpcService.java` - Service implementation
- `backend/src/main/resources/application.properties` - gRPC configuration

### 2. Envoy Proxy (Port 8080)
**Purpose**: Acts as a translation layer between gRPC-Web (browser) and gRPC (backend).

**Why Envoy is Essential**:
- Browsers cannot speak native gRPC protocol
- gRPC requires HTTP/2, which has limited browser support for bidirectional streaming
- gRPC-Web is a browser-compatible subset of gRPC
- Envoy translates gRPC-Web requests to native gRPC calls
- Handles CORS headers for cross-origin requests

**Key Files**:
- `envoy.yaml` - Envoy configuration with gRPC-Web filter and CORS settings

### 3. React Frontend (Port 3000)
**Purpose**: Provides the user interface that communicates via gRPC-Web.

**Why gRPC-Web**:
- Type-safe client generation from .proto files
- Better than REST for complex data structures
- Automatic serialization/deserialization
- Built-in error handling
- Future-proof for adding streaming features

**Key Files**:
- `frontend/src/generated/` - Auto-generated TypeScript clients
- `frontend/src/App.tsx` - Main React component using gRPC client

## Component Deep Dive

### Backend Configuration

#### Maven Dependencies (`backend/pom.xml`)
```xml
<!-- Spring gRPC Server -->
<dependency>
    <groupId>org.springframework.grpc</groupId>
    <artifactId>spring-grpc-server-spring-boot-starter</artifactId>
</dependency>

<!-- gRPC Services -->
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-services</artifactId>
</dependency>
```

#### Application Properties
```properties
# gRPC server configuration
grpc.server.port=9090
grpc.server.reflection-service-enabled=true

# No web server needed (gRPC only)
# spring.main.web-application-type=none  # Commented out to allow actuator
```

#### Service Implementation
The `@GrpcService` annotation registers the service with Spring's gRPC server:
```java
@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {
    // Implementation of proto-defined methods
}
```

### Envoy Configuration (`envoy.yaml`)

#### Key Sections Explained:

1. **HTTP Connection Manager**: Handles incoming HTTP requests
2. **gRPC-Web Filter**: Converts gRPC-Web to gRPC
3. **CORS Filter**: Enables cross-origin requests from React
4. **Router Filter**: Routes requests to backend cluster

```yaml
# Critical: Backend cluster configuration
clusters:
- name: grpc_service
  connect_timeout: 0.25s
  type: logical_dns
  http2_protocol_options: {}  # Enable HTTP/2 for gRPC
  load_assignment:
    cluster_name: grpc_service
    endpoints:
    - lb_endpoints:
      - endpoint:
          address:
            socket_address:
              address: 192.168.1.87  # Host machine IP (not localhost!)
              port_value: 9090
```

**Important**: The address must be the host machine's IP, not `127.0.0.1`, because Envoy runs in Docker.

### Frontend Setup

#### Package Dependencies
```json
{
  "grpc-web": "^1.5.0",
  "google-protobuf": "^3.21.2",
  "@types/google-protobuf": "^3.15.12"
}
```

#### Code Generation Script
```json
{
  "scripts": {
    "proto:generate": "protoc --js_out=import_style=commonjs:src/generated --grpc-web_out=import_style=typescript,mode=grpcwebtext:src/generated --proto_path=../backend/src/main/proto ../backend/src/main/proto/user_service.proto"
  }
}
```

#### Client Usage
```typescript
const grpcClient = new UserServiceClient('http://localhost:8080', null, null);

const loadUsers = async () => {
  const request = new GetAllUsersRequest();
  const response = await grpcClient.getAllUsers(request, {});
  // Handle response...
};
```

## Development Workflow

### 1. Protocol Buffer Changes
When you modify the `.proto` file:

1. **Backend**: Maven automatically regenerates Java classes during build
2. **Frontend**: Run `npm run proto:generate` to regenerate TypeScript clients
3. **Both**: Update service implementations and client code as needed

### 2. Development Flow
```bash
# 1. Start backend
cd backend
mvn spring-boot:run

# 2. Start Envoy (in another terminal)
docker run -it --rm -p 8080:8080 -p 9901:9901 \
  -v $(pwd)/envoy.yaml:/etc/envoy/envoy.yaml \
  envoyproxy/envoy:v1.28-latest -c /etc/envoy/envoy.yaml

# 3. Start frontend (in another terminal)
cd frontend
npm start
```

### 3. Testing the Stack
- **Frontend**: http://localhost:3000
- **Envoy Admin**: http://localhost:9901 (for debugging)
- **Backend Health**: Use grpcurl or direct gRPC client testing

## Troubleshooting Common Issues

### 1. 503 Service Unavailable
**Cause**: Envoy cannot connect to backend
**Solutions**:
- Verify backend is running on port 9090
- Check Envoy configuration has correct host IP (not 127.0.0.1)
- Use `docker logs <container-id>` to check Envoy logs

### 2. CORS Errors
**Cause**: Frontend origin not allowed
**Solution**: Update CORS configuration in `envoy.yaml`:
```yaml
cors:
  allow_origin_string_match:
  - prefix: "http://localhost:3000"  # Add your frontend URL
```

### 3. Method Not Found
**Cause**: Mismatch between proto definition and implementation
**Solutions**:
- Verify proto file is identical between backend and frontend
- Regenerate code after proto changes
- Check service method names match exactly

### 4. Connection Refused
**Cause**: Service not running or wrong port
**Solutions**:
- Verify all three services are running
- Check port conflicts with `netstat -an | grep LISTEN`
- Verify Docker networking for Envoy

## Production Considerations

### Security
- Add authentication/authorization to gRPC services
- Use TLS for gRPC communication
- Implement proper CORS policies
- Add rate limiting in Envoy

### Deployment
- Use Docker Compose for orchestration
- Configure health checks for all services
- Set up service discovery for dynamic backend addresses
- Add monitoring and logging

### Performance
- Configure connection pooling in Envoy
- Optimize protobuf message sizes
- Implement gRPC streaming for large datasets
- Add caching layers where appropriate

## Benefits of This Architecture

1. **Type Safety**: End-to-end type safety from database to UI
2. **Performance**: HTTP/2 multiplexing and binary protocol
3. **Code Generation**: Automatic client/server code generation
4. **Future-Proof**: Easy to add streaming, new services, or other language clients
5. **Developer Experience**: Strong tooling and IDE support
6. **Polyglot**: Easy to add services in other languages

## Next Steps

1. Add authentication (JWT tokens)
2. Implement streaming endpoints
3. Add database persistence
4. Set up Docker Compose for easy deployment
5. Add comprehensive error handling
6. Implement proper logging and monitoring

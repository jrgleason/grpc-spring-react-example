# gRPC-Web Full-Stack Application

A modern full-stack application demonstrating **pure gRPC communication** between a React frontend and Spring Boot backend, with no REST endpoints. Features type-safe communication using Protocol Buffers and gRPC-Web.

**🆕 Now with Apollo GraphQL Federation support!** Choose between gRPC-Web or GraphQL for your frontend.

## 🏗️ Architecture Options

### Option 1: gRPC-Web (Original)
```
React App (TypeScript) ←→ Envoy Proxy ←→ Spring Boot (gRPC Server)
    Port 3000              Port 8080         Port 9090
```

### Option 2: GraphQL Federation (New)
```
React App (GraphQL) ←→ Apollo Gateway ←→ GraphQL Service ←→ gRPC Service
    Port 3000            Port 4000         Port 4001       Port 9090
```

- **Frontend**: React with TypeScript + Apollo Client or gRPC-Web client
- **GraphQL Layer**: Apollo Federation Gateway + GraphQL services (optional)
- **Proxy**: Envoy for gRPC-Web ↔ gRPC translation  
- **Backend**: Spring Boot with native gRPC server (no web/REST)
- **Protocol**: 100% gRPC communication with Protocol Buffers

## 📋 Prerequisites

- **Java 24+** (for Spring Boot backend)
- **Node.js 18+** (for React frontend)
- **Docker** (for Envoy proxy)
- **Maven** (for backend build)
- **protoc** (Protocol Buffer compiler) - installed via npm

## 🚀 Quick Start

### 1. Clone and Setup
```bash
git clone <repository>
cd stream-example
```

### 2. Start Backend (Terminal 1)
```bash
cd backend
mvn spring-boot:run
# Backend starts on port 9090
```

### 3. Start Envoy Proxy (Terminal 2)
```bash
# From project root
docker run -it --rm -p 8080:8080 -p 9901:9901 \
  -v $(pwd)/envoy.yaml:/etc/envoy/envoy.yaml \
  envoyproxy/envoy:v1.28-latest -c /etc/envoy/envoy.yaml
# Envoy starts on port 8080
```

### 4. Start Frontend (Terminal 3)
```bash
cd frontend
npm install
npm start
# Frontend starts on port 3000
```

### 5. Open Application
Navigate to **http://localhost:3000** and test the user management features.

## 🚀 Apollo GraphQL Federation (Alternative)

For a GraphQL layer on top of your gRPC services:

### Quick Start with Apollo
```bash
# One-command setup
./start-apollo.sh
```

This starts:
- **Apollo Gateway**: http://localhost:4000/graphql
- **User GraphQL Service**: http://localhost:4001/graphql  
- **Frontend (GraphQL)**: http://localhost:3000
- **gRPC Backend**: http://localhost:9090
- **Envoy Proxy**: http://localhost:8080 (for legacy support)

### Manual Apollo Setup
```bash
# 1. Start gRPC backend
cd backend && mvn spring-boot:run

# 2. Start with Docker Compose
docker-compose -f docker-compose.graphql.yml up --build

# 3. Access services
open http://localhost:4000/graphql  # Apollo Gateway
open http://localhost:3000          # GraphQL Frontend
```

See **[GRAPHQL_API.md](./GRAPHQL_API.md)** for complete GraphQL documentation.

## 📚 Documentation

- **[INSTRUCTIONS.md](./INSTRUCTIONS.md)** - Comprehensive guide explaining architecture, components, and development workflow
- **[Docker Setup](#docker-alternative)** - Alternative Docker Compose setup

## Project Structure

```
stream-example/
├── backend/                    # Spring Boot gRPC Server
│   ├── src/main/
│   │   ├── java/              # Service implementations
│   │   ├── proto/             # Protocol Buffer definitions (.proto)
│   │   └── resources/         # Application configuration
│   ├── Dockerfile
│   └── pom.xml               # Maven dependencies (gRPC, Spring Boot)
├── frontend/                  # React TypeScript App (gRPC-Web)
│   ├── src/
│   │   ├── generated/        # Auto-generated gRPC-Web clients
│   │   ├── App.tsx          # Main React component
│   │   └── App.css          # Styling
│   └── package.json         # npm dependencies (gRPC-Web, React)
├── apollo-gateway/            # Apollo Federation Gateway
│   ├── src/index.ts          # Gateway configuration
│   ├── Dockerfile
│   └── package.json         # Apollo Gateway dependencies
├── user-graphql-service/      # GraphQL service wrapping gRPC
│   ├── src/
│   │   ├── schema.ts        # GraphQL schema definition
│   │   ├── resolvers.ts     # GraphQL resolvers
│   │   └── grpc-client.ts   # gRPC client wrapper
│   ├── Dockerfile
│   └── package.json         # GraphQL service dependencies
├── frontend-graphql/          # React TypeScript App (GraphQL)
│   ├── src/
│   │   ├── App.tsx          # GraphQL-powered React component
│   │   └── index.tsx        # Apollo Client setup
│   ├── Dockerfile
│   └── package.json         # Apollo Client dependencies
├── envoy.yaml               # Envoy proxy configuration
├── docker-compose.yml       # Original gRPC-Web setup
├── docker-compose.graphql.yml # Apollo Federation setup
├── start-apollo.sh          # One-command Apollo setup
├── INSTRUCTIONS.md          # Detailed architecture guide
├── ADDING_APOLLO.md         # Apollo Federation evolution guide
├── GRAPHQL_API.md          # GraphQL API documentation
└── README.md               # This file
```

## ✨ Key Features

### 🔧 Backend (Spring Boot + gRPC)
- Pure gRPC server (no REST/web endpoints)
- Spring Boot 3.5 with official gRPC support
- Type-safe Protocol Buffer definitions
- UserService with full CRUD operations
- Automatic Java code generation from .proto

### 🎨 Frontend (React + gRPC-Web)
- Modern React 18 with TypeScript
- Type-safe gRPC-Web client
- Auto-generated client code from .proto
- Responsive Material-inspired UI
- Real-time communication

### 🔀 Envoy Proxy
- gRPC-Web ↔ gRPC protocol translation
- CORS handling for browser requests
- HTTP/2 and connection management
- Request/response logging

## 🛠️ Development

### Code Generation Workflow
When you modify `user_service.proto`:

1. **Backend**: Maven auto-generates Java classes on build
2. **Frontend**: Run `npm run proto:generate` for TypeScript clients

### Environment Setup
```bash
# Backend dependencies
mvn clean install

# Frontend dependencies  
cd frontend && npm install

# Install Protocol Buffer compiler (if needed)
npm install -g protoc
```

## Docker Alternative

1. Start all services:
   ```bash
   docker-compose up --build
   ```

This will start:
- gRPC backend on port 9090
- Envoy proxy on port 8080 (gRPC-Web endpoint)
- React frontend on port 3000

### Option 2: Manual Setup

#### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

Use Docker Compose for simplified multi-service setup:

```bash
# Build and start all services
docker-compose up --build

# Stop all services
docker-compose down
```

Services will be available at:
- Frontend: http://localhost:3000
- Envoy Proxy: http://localhost:8080
- Backend: gRPC on port 9090

## 🧪 Testing the Application

### Manual Testing
1. Open http://localhost:3000
2. Click "Load Users" to fetch users via gRPC
3. Use the form to create/edit users
4. Test all CRUD operations

### gRPC Testing (Optional)
```bash
# Using grpcurl (if installed)
grpcurl -plaintext localhost:9090 list
grpcurl -plaintext localhost:9090 org.jrg.grpc.UserService/GetAllUsers
```

### Debugging
- **Envoy Admin Interface**: http://localhost:9901
- **Backend Logs**: Check console output
- **Frontend Console**: Browser developer tools

## 🚧 Troubleshooting

| Issue | Solution |
|-------|----------|
| **503 Service Unavailable** | Check if backend is running on port 9090 |
| **CORS Errors** | Verify Envoy CORS configuration |
| **Connection Refused** | Ensure Envoy can reach backend (check IP in envoy.yaml) |
| **Method Not Found** | Regenerate proto clients after changes |

For detailed troubleshooting, see [INSTRUCTIONS.md](./INSTRUCTIONS.md).

## 🔧 gRPC Service Definition

### gRPC Service (via gRPC-Web on port 8080)
The `user_service.proto` file defines:

```protobuf
service UserService {
  rpc GetUser(GetUserRequest) returns (User);
  rpc GetAllUsers(GetAllUsersRequest) returns (GetAllUsersResponse);
  rpc CreateUser(CreateUserRequest) returns (User);
  rpc UpdateUser(UpdateUserRequest) returns (User);
  rpc DeleteUser(DeleteUserRequest) returns (DeleteUserResponse);
  rpc StreamUsers(StreamUsersRequest) returns (stream User);
}

message User {
  int64 id = 1;
  string name = 2;
  string email = 3;
  string role = 4;
  string created_at = 5;
}
```

All gRPC calls are routed through Envoy proxy at http://localhost:8080

## 🎯 Usage Examples

### Frontend (React/TypeScript)
```typescript
// Auto-generated client from .proto
const client = new UserServiceClient('http://localhost:8080');

// Type-safe gRPC call
const request = new GetAllUsersRequest();
const response = await client.getAllUsers(request, {});
const users = response.getUsersList();
```

### Backend (Spring Boot/Java)
```java
@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {
    @Override
    public void getAllUsers(GetAllUsersRequest request, 
                           StreamObserver<GetAllUsersResponse> responseObserver) {
        // Implementation
    }
}
```

## 📈 Benefits of This Architecture

- **Type Safety**: End-to-end type safety from database to UI
- **Performance**: HTTP/2 multiplexing and binary protocol  
- **Code Generation**: Automatic client/server stub generation
- **Future-Proof**: Easy to add streaming, new services, other languages
- **Developer Experience**: Strong IDE support and tooling

## 🔗 Additional Resources

- **[Protocol Buffers Guide](https://protobuf.dev/)**
- **[gRPC Documentation](https://grpc.io/docs/)**
- **[Spring gRPC](https://github.com/grpc-ecosystem/grpc-spring)**
- **[Envoy Proxy](https://www.envoyproxy.io/docs/)**

## 📝 License

This project is for demonstration purposes.

### Frontend gRPC Client Generation
The frontend uses `protoc` with `protoc-gen-grpc-web` to generate TypeScript client code from `.proto` files. Generated files are placed in `src/generated/`.


## Future Enhancements

1. **Server Streaming**: Implement real-time user updates via gRPC streaming
2. **Authentication**: Add JWT or OAuth2 authentication with gRPC interceptors
3. **Database**: Replace in-memory storage with JPA/Hibernate
4. **Validation**: Add input validation and error handling
5. **Testing**: Add unit and integration tests for gRPC services
6. **TLS**: Enable TLS for production gRPC communication
7. **Load Balancing**: Add multiple backend instances with Envoy load balancing

## License

This project is for demonstration purposes and is provided as-is.

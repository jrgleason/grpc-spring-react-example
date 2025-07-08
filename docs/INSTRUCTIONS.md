# GraphQL Federation Full-Stack Application Instructions

This document explains the architecture, components, and setup process for this GraphQL Federation full-stack application.

## Prerequisites

Before running this application, ensure you have the following installed:

- **Java 24+** - Required for the Spring Boot backend
- **Maven 3.9+** - Required for building the backend (must be installed system-wide)
- **Node.js 22+** - Required for GraphQL services and frontend (supports Node.js 24)
- **Docker & Docker Compose** - Required for containerized deployment

> **Maven Installation**: This project requires Maven to be installed on your system. The Maven Wrapper (`mvnw`) is not used.
> 
> **Install Maven**:
> - **macOS**: `brew install maven`
> - **Ubuntu/Debian**: `sudo apt install maven`  
> - **Windows**: Download from [maven.apache.org](https://maven.apache.org/install.html)
> - **Manual**: Follow instructions at [maven.apache.org/install.html](https://maven.apache.org/install.html)

## Architecture Overview

This application demonstrates a modern GraphQL Federation architecture with:
- **Frontend**: React with Apollo Client for GraphQL queries
- **API Gateway**: Apollo Federation Gateway aggregating multiple GraphQL services
- **Microservices**: Individual GraphQL services that wrap gRPC backend calls
- **Backend**: Spring Boot with gRPC server (Java 24)

```mermaid
graph LR
    A[React App<br/>Port 3000] <-->|GraphQL| B[Apollo Gateway<br/>Port 4000]
    B <-->|GraphQL| C[User GraphQL Service<br/>Port 4001]
    C <-->|gRPC| D[Spring Boot Backend<br/>Port 9090]
    
    style A fill:#61dafb,stroke:#333,stroke-width:2px,color:#000
    style B fill:#311c87,stroke:#333,stroke-width:2px,color:#fff
    style C fill:#e10098,stroke:#333,stroke-width:2px,color:#fff
    style D fill:#6db33f,stroke:#333,stroke-width:2px,color:#fff
    style E fill:#ac6199,stroke:#333,stroke-width:2px,color:#fff
```

## Why Each Component is Needed

### 1. Spring Boot Backend (Port 9090)
**Purpose**: Serves as the gRPC server that handles business logic and data operations.

**Why gRPC for Internal Communication**:
- Type-safe communication with Protocol Buffers
- Better performance with HTTP/2 for service-to-service communication
- Automatic code generation for multiple languages
- Built-in streaming capabilities
- Strong contract definition with .proto files

**Key Files**:
- `backend/src/main/proto/user_service.proto` - Service definition
- `backend/src/main/java/org/jrg/grpc/service/UserGrpcService.java` - Service implementation
- `backend/src/main/resources/application.properties` - gRPC configuration

### 2. User GraphQL Service (Port 4001)
**Purpose**: Wraps the gRPC backend with a GraphQL API and participates in Apollo Federation.

**Why GraphQL Layer**:
- Provides a unified, client-friendly API
- Supports Apollo Federation for schema composition
- Enables flexible queries and better developer experience
- Abstracts underlying gRPC complexity from frontend

**Key Files**:
- `user-graphql-service/src/index.js` - GraphQL service server
- `user-graphql-service/src/schema.js` - GraphQL schema definition
- `user-graphql-service/src/resolvers.js` - GraphQL resolvers
- `user-graphql-service/src/grpc-client.js` - gRPC client for backend communication

### 3. Apollo Gateway (Port 4000)
**Purpose**: Aggregates multiple GraphQL services into a single federated schema.

**Why Apollo Federation**:
- Enables microservices architecture with unified GraphQL API
- Allows teams to work on separate services independently
- Provides schema composition and type merging
- Supports distributed GraphQL across multiple services

**Key Files**:
- `apollo-gateway/src/index.js` - Gateway server configuration
- `docker-compose.graphql.yml` - Full stack deployment

### 4. React Frontend (Port 3000)
**Purpose**: Provides the user interface using Apollo Client for GraphQL queries.

**Why Apollo Client**:
- Single GraphQL endpoint for all data needs
- Automatic query optimization and caching
- Better developer experience with GraphQL tooling
- Flexible querying capabilities
- Type-safe operations with code generation

**Key Files**:
- `frontend-graphql/src/` - React components using Apollo Client
- `frontend-graphql/src/graphql/` - GraphQL queries and mutations
- `frontend-graphql/package.json` - Apollo Client dependencies

## Development Workflow

### Starting the Full Stack

#### Option 1: Docker Compose (Recommended)
```bash
# Start the entire GraphQL Federation stack
docker-compose -f docker-compose.graphql.yml up --build

# Services available at:
# - Frontend: http://localhost:3000
# - Apollo Gateway: http://localhost:4000/graphql
# - User GraphQL Service: http://localhost:4001/graphql
# - gRPC Backend: localhost:9090
```

#### Option 2: Manual Development Setup
```bash
# Terminal 1: Start Backend
cd backend
mvn spring-boot:run

# Terminal 2: Start User GraphQL Service
cd user-graphql-service
npm install
npm start

# Terminal 3: Start Apollo Gateway
cd apollo-gateway
npm install
npm start

# Terminal 4: Start Frontend
cd frontend-graphql
npm install
npm start
```

### Adding New GraphQL Services

1. **Create New Service**:
   ```bash
   mkdir new-service-graphql
   cd new-service-graphql
   npm init -y
   npm install @apollo/subgraph graphql apollo-server-express
   ```

2. **Implement Federated Schema**:
   ```javascript
   // new-service-graphql/src/schema.js
   import { buildSubgraphSchema } from '@apollo/subgraph';
   import { gql } from 'apollo-server-express';
   
   const typeDefs = gql`
     extend type Query {
       newServiceOperation: String
     }
   `;
   
   const resolvers = {
     Query: {
       newServiceOperation: () => "Hello from new service"
     }
   };
   
   export const schema = buildSubgraphSchema({ typeDefs, resolvers });
   ```

3. **Update Apollo Gateway**:
   ```javascript
   // apollo-gateway/src/index.js
   const subgraphs = [
     { name: 'users', url: 'http://user-graphql-service:4001/graphql' },
     { name: 'newservice', url: 'http://new-service-graphql:4002/graphql' },
   ];
   ```

4. **Update Docker Compose**:
   ```yaml
   # docker-compose.graphql.yml
   new-service-graphql:
     build: ./new-service-graphql
     ports:
       - "4002:4002"
     environment:
       - PORT=4002
   ```

### Testing the GraphQL Federation

1. **Apollo Gateway Playground**:
   - Open http://localhost:4000/graphql
   - Use GraphQL Playground to test queries across services

2. **Individual Service Testing**:
   ```bash
   # Test user service directly
   curl -X POST http://localhost:4001/graphql \
     -H "Content-Type: application/json" \
     -d '{"query": "{ users { id name email } }"}'
   ```

3. **Frontend Integration**:
   ```bash
   # Check frontend GraphQL queries
   cd frontend-graphql
   npm run codegen  # Generate TypeScript types from GraphQL schema
   ```

## CI/CD Pipeline

The project includes a GitHub Actions workflow that:
- Builds and tests all Node.js services
- Builds and tests the Java backend
- Runs integration tests across services
- Performs security scanning
- Builds and pushes Docker images

**Key Files**:
- `.github/workflows/ci-cd.yml` - Main CI/CD pipeline
- `.github/workflows/README.md` - Pipeline documentation

## Protocol Buffer Integration

While the frontend uses GraphQL, the backend services still communicate via gRPC:

1. **Proto File Changes**:
   ```bash
   # Backend (automatic with Maven)
   cd backend
   mvn clean compile  # Regenerates Java classes
   
   # GraphQL Service
   cd user-graphql-service
   npm run proto:generate  # If using protoc directly
   ```

2. **Schema Updates**:
   - Update `.proto` files in `backend/src/main/proto/`
   - Regenerate gRPC clients in GraphQL services
   - Update GraphQL schema to match new proto definitions
   - Update resolvers to handle new fields/operations

## Monitoring and Debugging

### GraphQL Federation Debugging
```bash
# Check gateway schema composition
curl http://localhost:4000/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ _service { sdl } }"}'

# Check service health
curl http://localhost:4001/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ __typename }"}'
```

### Backend gRPC Debugging
```bash
# Test gRPC services directly
grpcurl -plaintext localhost:9090 list
grpcurl -plaintext localhost:9090 org.jrg.grpc.UserService/GetAllUsers
```

### Common Issues
- **Schema conflicts**: Ensure type names don't conflict across services
- **Service discovery**: Verify all services are reachable by Apollo Gateway
- **gRPC connectivity**: Check that GraphQL services can reach gRPC backend
- **Apollo Federation**: Ensure `@apollo/subgraph` is used correctly

## Production Considerations

1. **Schema Registry**: Use Apollo Studio for schema management
2. **Caching**: Implement Redis for Apollo Gateway caching
3. **Monitoring**: Add Apollo Studio metrics and tracing
4. **Security**: Implement authentication/authorization across services
5. **Scaling**: Use Kubernetes for service orchestration

## Benefits of This Architecture

1. **Unified API**: Single GraphQL endpoint for all client needs
2. **Microservices**: Independent service development and deployment
3. **Type Safety**: End-to-end type safety from database to UI
4. **Performance**: Efficient queries with GraphQL and fast gRPC backend
5. **Developer Experience**: Great tooling and introspection capabilities
6. **Scalability**: Easy to add new services and scale independently

## Migration from gRPC-Web

If you have existing gRPC-Web clients, you can migrate gradually:

1. **Phase 1**: Add GraphQL layer while keeping existing gRPC services
2. **Phase 2**: Migrate frontend components to use GraphQL instead of direct gRPC calls  
3. **Phase 3**: Standardize on GraphQL Federation architecture

The GraphQL services act as adapters, making the migration seamless.

## Development Workflow

### 1. Protocol Buffer Changes
When you modify the `.proto` file:

1. **Backend**: Maven automatically regenerates Java classes during build
2. **GraphQL Services**: Update GraphQL resolvers to match new gRPC service definitions
3. **Frontend**: Apollo Client automatically handles GraphQL schema changes via introspection

### 2. Development Flow
```bash
# Use Docker Compose for full stack development
docker-compose -f docker-compose.graphql.yml up --build

# Or start services individually:
# 1. Backend
cd backend && mvn spring-boot:run

# 2. User GraphQL Service
cd user-graphql-service && npm start

# 3. Apollo Gateway
cd apollo-gateway && npm start

# 4. Frontend
cd frontend-graphql && npm start
```

### 3. Testing the Stack
- **Frontend**: http://localhost:3000
- **GraphQL Playground**: http://localhost:4000/graphql
- **User Service GraphQL**: http://localhost:4001/graphql
- **Backend Health**: Use grpcurl or gRPC client testing

## Troubleshooting Common Issues

### 1. GraphQL Service Connection Errors
**Cause**: GraphQL service cannot connect to gRPC backend
**Solutions**:
- Verify backend is running on port 9090
- Check service configuration has correct gRPC service URL
- Use `docker logs <container-id>` to check service logs

### 2. Schema Federation Errors
**Cause**: Gateway cannot compose schemas from services
**Solutions**:
- Verify all GraphQL services are running and accessible
- Check gateway configuration for correct service URLs
- Ensure GraphQL schemas are properly federated with `@key` directives

### 3. GraphQL Query Errors
**Cause**: Query doesn't match available schema
**Solutions**:
- Use GraphQL Playground to explore available schema
- Verify query syntax and field availability
- Check resolvers are properly implemented in GraphQL services
- Check service method names match exactly

### 4. Connection Refused
**Cause**: Service not running or wrong port
**Solutions**:
- Verify all services are running
- Check port conflicts with `netstat -an | grep LISTEN`
- Verify Docker networking between services

## Production Considerations

### Security
- Add authentication/authorization to GraphQL services
- Use TLS for gRPC communication
- Implement proper CORS policies
- Add rate limiting to Apollo Gateway

### Deployment
- Use Docker Compose for orchestration
- Configure health checks for all services
- Set up service discovery for dynamic backend addresses
- Add monitoring and logging

### Performance
- Configure connection pooling for gRPC services
- Optimize protobuf message sizes
- Implement GraphQL subscriptions for real-time data
- Add caching layers in Apollo Gateway

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

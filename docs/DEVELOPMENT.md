# Development Guide

This guide covers local development setup, workflows, and best practices for the GraphQL Federation project.

## Prerequisites

- **Java 24+** (for Spring Boot backend)
- **Node.js 22+** (for GraphQL services and frontend)
- **Docker & Docker Compose** (for containerized deployment)
- **Maven 3.9+** (must be installed system-wide for backend build)

> **Maven Installation**: This project requires Maven to be installed on your system. The Maven Wrapper (`mvnw`) is not used.
> 
> **Install Maven**:
> - **macOS**: `brew install maven`
> - **Ubuntu/Debian**: `sudo apt install maven`  
> - **Windows**: Download from [maven.apache.org](https://maven.apache.org/install.html)
> - **Manual**: Follow instructions at [maven.apache.org/install.html](https://maven.apache.org/install.html)

## Development Setup

### Option 1: Docker Compose (Recommended)
```bash
# Start entire stack with one command
docker-compose -f docker-compose.graphql.yml up --build

# Services will be available at:
# Frontend: http://localhost:3000
# Apollo Gateway: http://localhost:4000/graphql
# User Service: http://localhost:4001/graphql
# gRPC Backend: grpc://localhost:9090
```

### Option 2: Manual Development Setup
```bash
# 1. Start gRPC Backend (Terminal 1)
cd backend
mvn spring-boot:run

# 2. Start User GraphQL Service (Terminal 2)
cd user-graphql-service
npm install && npm start

# 3. Start Apollo Gateway (Terminal 3)
cd apollo-gateway
npm install && npm start

# 4. Start Frontend (Terminal 4)
cd frontend-graphql
npm install && npm start
```

## Local Development Workflow

### Backend Development
```bash
# Start backend only
cd backend && mvn spring-boot:run

# Run tests
cd backend && mvn test

# Build
cd backend && mvn clean package
```

### GraphQL Services Development
```bash
# Start GraphQL services
cd user-graphql-service && npm run dev
cd apollo-gateway && npm run dev

# Start frontend with hot reload
cd frontend-graphql && npm start
```

## Protocol Buffer Development

### Code Generation
Protocol Buffer changes automatically trigger:
- **Java code generation** (Maven compile)
- **gRPC service stubs** (Spring gRPC)

When you modify `.proto` files:
1. **Backend**: Maven automatically regenerates Java classes during build
2. **GraphQL Services**: Update GraphQL resolvers to match new gRPC service definitions
3. **Frontend**: Apollo Client automatically handles GraphQL schema changes via introspection

### Testing Protocol Changes
```bash
# Test backend gRPC services
grpcurl -plaintext localhost:9090 list
grpcurl -plaintext localhost:9090 org.jrg.grpc.UserService/GetAllUsers

# Test GraphQL services
curl -X POST http://localhost:4001/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ users { id name email } }"}'

# Test full federation
curl -X POST http://localhost:4000/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ users { id name email } }"}'
```

## Adding New Features

### Adding a New GraphQL Service

1. **Create Service Directory**:
   ```bash
   mkdir new-service-graphql
   cd new-service-graphql
   npm init -y
   ```

2. **Install Dependencies**:
   ```bash
   npm install @apollo/subgraph graphql apollo-server-express
   ```

3. **Implement Federated Schema**:
   ```javascript
   // src/schema.js
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

4. **Update Apollo Gateway**:
   ```javascript
   // apollo-gateway/src/index.js
   const subgraphs = [
     { name: 'users', url: 'http://user-graphql-service:4001/graphql' },
     { name: 'newservice', url: 'http://new-service-graphql:4002/graphql' },
   ];
   ```

5. **Add to Docker Compose**:
   ```yaml
   # docker-compose.graphql.yml
   new-service-graphql:
     build: ./new-service-graphql
     ports:
       - "4002:4002"
     environment:
       - PORT=4002
   ```

### Adding Backend gRPC Services

1. **Update Proto File**:
   ```protobuf
   // backend/src/main/proto/new_service.proto
   service NewService {
     rpc NewOperation(NewRequest) returns (NewResponse);
   }
   ```

2. **Implement Service**:
   ```java
   @GrpcService
   public class NewGrpcService extends NewServiceGrpc.NewServiceImplBase {
     @Override
     public void newOperation(NewRequest request, 
                            StreamObserver<NewResponse> responseObserver) {
       // Implementation
     }
   }
   ```

3. **Add GraphQL Wrapper**:
   - Create gRPC client in GraphQL service
   - Add GraphQL schema definitions
   - Implement resolvers

## Testing

### Unit Testing
```bash
# Backend tests
cd backend && mvn test

# Node.js services tests
cd user-graphql-service && npm test
cd apollo-gateway && npm test
cd frontend-graphql && npm test
```

### Integration Testing
```bash
# Full stack with Docker Compose
docker-compose -f docker-compose.graphql.yml up --build

# Health checks
curl http://localhost:4000/health  # Gateway
curl http://localhost:4001/health  # User service
curl http://localhost:3000         # Frontend
```

### Manual Testing

#### GraphQL Playground
- Open http://localhost:4000/graphql
- Explore schema and run queries
- Test mutations and subscriptions

#### Sample Queries
```graphql
# Get all users
query GetUsers {
  users {
    id
    name
    email
    role
    createdAt
  }
}

# Create a user
mutation CreateUser {
  createUser(input: {
    name: "John Doe"
    email: "john@example.com"
    role: "USER"
  }) {
    id
    name
    email
  }
}
```

## Debugging

### Common Issues
- **GraphQL Service Connection Errors**: Check gRPC backend connectivity
- **Schema Federation Errors**: Verify service URLs and federation directives
- **Port Conflicts**: Use `netstat -an | grep LISTEN` to check ports
- **Docker Issues**: Check `docker logs <container-name>` for errors

### Debugging Tools
- **GraphQL Playground**: http://localhost:4000/graphql
- **Apollo Studio**: For production schema management
- **grpcurl**: For testing gRPC services directly
- **Docker logs**: `docker-compose logs <service-name>`

## Code Style and Standards

### Backend (Java)
- Follow Spring Boot conventions
- Use `@GrpcService` for service implementations
- Implement proper error handling with gRPC status codes
- Add comprehensive logging

### Frontend (TypeScript/React)
- Use Apollo Client hooks (`useQuery`, `useMutation`)
- Implement proper loading and error states
- Use TypeScript for type safety
- Follow React best practices

### GraphQL Services (Node.js)
- Use Apollo Federation directives properly
- Implement health check endpoints
- Add proper error handling
- Use environment variables for configuration

## Performance Optimization

### Backend
- Use connection pooling for database access
- Implement gRPC streaming for large datasets
- Add proper caching where appropriate
- Monitor gRPC metrics

### Frontend
- Use Apollo Client caching effectively
- Implement pagination for large lists
- Use React.memo for expensive components
- Optimize bundle size

### GraphQL Services
- Implement DataLoader for N+1 query prevention
- Add caching at the GraphQL layer
- Use proper error handling and logging
- Monitor query performance

## Security Considerations

### Development
- Never commit sensitive data (API keys, passwords)
- Use environment variables for configuration
- Implement proper CORS policies
- Validate all inputs

### Production
- Use TLS for all communications
- Implement authentication and authorization
- Add rate limiting
- Regular security updates

## 🐋 Docker Deployment

### Production Build
```bash
# Build all services
docker-compose -f docker-compose.graphql.yml build

# Start in production mode
docker-compose -f docker-compose.graphql.yml up -d

# Check service health
curl http://localhost:4000/health
curl http://localhost:4001/health
curl http://localhost:3000
```

### Multi-Stage Builds
All services use optimized multi-stage Docker builds:
- **Backend**: Java build stage + runtime stage
- **Node.js Services**: Dependency caching + production builds
- **Frontend**: Vite build + Nginx serving

### Docker Issues
Common Docker issues and solutions:

| Issue | Solution |
|-------|----------|
| **Build failures** | Clean Docker cache: `docker system prune` |
| **Port conflicts** | Check ports are not in use: `lsof -i :9090` |
| **Memory issues** | Increase Docker memory allocation |
| **Network issues** | Reset Docker networks: `docker network prune` |

### Service Management
```bash
# Start specific services only
docker-compose -f docker-compose.graphql.yml up backend user-graphql-service

# View logs for specific service
docker-compose -f docker-compose.graphql.yml logs -f apollo-gateway

# Restart a single service
docker-compose -f docker-compose.graphql.yml restart frontend-graphql

# Scale services (if needed)
docker-compose -f docker-compose.graphql.yml up --scale user-graphql-service=2
```

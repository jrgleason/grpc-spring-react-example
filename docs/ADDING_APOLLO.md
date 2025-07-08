# Adding Apache Apollo GraphQL Federation to gRPC Services

This document outlines how to evolve the current gRPC-Web architecture to include Apache Apollo GraphQL Federation, creating a modern GraphQL service mesh.

## Evolution Path Overview

### Current Architecture
```
Frontend (gRPC-Web) → Envoy → Backend (gRPC)
```

### Target Architecture with Apollo Federation
```
Frontend (GraphQL) → Apollo Gateway → GraphQL Services → gRPC Services
                                   → Istio Service Mesh →
```

## Why Add GraphQL to gRPC?

### Benefits of GraphQL Layer
1. **Client-Friendly API**: Single endpoint, flexible queries, strong typing
2. **API Composition**: Combine multiple gRPC services into unified schema
3. **Frontend Optimization**: Reduce over-fetching, better caching
4. **Developer Experience**: Better tooling, introspection, documentation
5. **Evolution Path**: Migrate clients gradually from gRPC-Web to GraphQL

### gRPC as Internal APIs
- **Keep gRPC services** as internal communication layer
- **High performance** between services
- **Type safety** with Protocol Buffers
- **Streaming capabilities** for real-time features
- **Service contracts** remain stable

## Architecture Components

### 1. Apollo Federation Gateway
```typescript
// apollo-gateway/src/index.ts
import { ApolloGateway, IntrospectAndCompose } from '@apollo/gateway';
import { ApolloServer } from 'apollo-server-express';

const gateway = new ApolloGateway({
  supergraphSdl: new IntrospectAndCompose({
    subgraphs: [
      { name: 'users', url: 'http://user-graphql-service:4001/graphql' },
      { name: 'orders', url: 'http://order-graphql-service:4002/graphql' },
      { name: 'products', url: 'http://product-graphql-service:4003/graphql' },
    ],
  }),
});

const server = new ApolloServer({
  gateway,
  subscriptions: false, // Disable subscriptions for now
});
```

### 2. GraphQL Services (Wrapping gRPC)
```typescript
// user-graphql-service/src/resolvers.ts
import { UserServiceClient } from './generated/grpc/UserServiceClient';
import { GetUserRequest, GetAllUsersRequest } from './generated/grpc/user_service_pb';

const grpcClient = new UserServiceClient('user-grpc-service:9090');

export const resolvers = {
  Query: {
    user: async (_, { id }) => {
      const request = new GetUserRequest();
      request.setId(id);
      
      const response = await grpcClient.getUser(request);
      return {
        id: response.getId(),
        name: response.getName(),
        email: response.getEmail(),
        role: response.getRole(),
      };
    },
    
    users: async () => {
      const request = new GetAllUsersRequest();
      const response = await grpcClient.getAllUsers(request);
      
      return response.getUsersList().map(user => ({
        id: user.getId(),
        name: user.getName(),
        email: user.getEmail(),
        role: user.getRole(),
      }));
    },
  },
  
  Mutation: {
    createUser: async (_, { input }) => {
      const request = new CreateUserRequest();
      request.setName(input.name);
      request.setEmail(input.email);
      request.setRole(input.role);
      
      const response = await grpcClient.createUser(request);
      return {
        id: response.getId(),
        name: response.getName(),
        email: response.getEmail(),
        role: response.getRole(),
      };
    },
  },
};
```

### 3. GraphQL Schema (Federation)
```graphql
# user-graphql-service/schema.graphql
extend type Query {
  user(id: ID!): User
  users: [User!]!
}

extend type Mutation {
  createUser(input: CreateUserInput!): User!
  updateUser(id: ID!, input: UpdateUserInput!): User!
  deleteUser(id: ID!): Boolean!
}

type User @key(fields: "id") {
  id: ID!
  name: String!
  email: String!
  role: UserRole!
  createdAt: String!
}

input CreateUserInput {
  name: String!
  email: String!
  role: UserRole!
}

input UpdateUserInput {
  name: String
  email: String
  role: UserRole
}

enum UserRole {
  USER
  ADMIN
  MODERATOR
}
```

## Service Mesh with Istio

### Why Istio for GraphQL Federation?

1. **Service Discovery**: Automatic service registration and discovery
2. **Load Balancing**: Distribute traffic across service instances
3. **Security**: mTLS, authentication, authorization policies
4. **Observability**: Metrics, tracing, logging across all services
5. **Traffic Management**: Canary deployments, circuit breaking
6. **Policy Enforcement**: Rate limiting, timeout policies

### Istio Configuration

#### 1. Service Mesh Setup
```yaml
# istio-config/gateway.yaml
apiVersion: networking.istio.io/v1beta1
kind: Gateway
metadata:
  name: apollo-gateway
spec:
  selector:
    istio: ingressgateway
  servers:
  - port:
      number: 80
      name: http
      protocol: HTTP
    hosts:
    - api.yourdomain.com
  - port:
      number: 443
      name: https
      protocol: HTTPS
    tls:
      mode: SIMPLE
      credentialName: api-tls-secret
    hosts:
    - api.yourdomain.com
```

#### 2. Virtual Services
```yaml
# istio-config/virtualservice.yaml
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
metadata:
  name: apollo-routes
spec:
  hosts:
  - api.yourdomain.com
  gateways:
  - apollo-gateway
  http:
  - match:
    - uri:
        prefix: /graphql
    route:
    - destination:
        host: apollo-gateway-service
        port:
          number: 4000
  - match:
    - uri:
        prefix: /
    route:
    - destination:
        host: apollo-gateway-service
        port:
          number: 4000
```

#### 3. Service Mesh Policies
```yaml
# istio-config/policies.yaml
apiVersion: security.istio.io/v1beta1
kind: PeerAuthentication
metadata:
  name: default
  namespace: production
spec:
  mtls:
    mode: STRICT

---
apiVersion: security.istio.io/v1beta1
kind: AuthorizationPolicy
metadata:
  name: grpc-services-policy
spec:
  rules:
  - from:
    - source:
        principals: ["cluster.local/ns/production/sa/graphql-service"]
  - to:
    - operation:
        methods: ["*"]
```

## Migration Strategy

### Phase 1: Add GraphQL Layer (Keep gRPC-Web)
```
Frontend (gRPC-Web) → Envoy → gRPC Services
Frontend (GraphQL)   → Apollo → GraphQL Services → gRPC Services
```

### Phase 2: Migrate to Istio
```
Frontend (GraphQL) → Istio Gateway → Apollo → GraphQL Services → gRPC Services
                                                              ↓
                                                         Istio Service Mesh
```

### Phase 3: Full Service Mesh
```
Frontend → Istio → Apollo Gateway → Federation → Multiple GraphQL Services
                                                     ↓
                                               Multiple gRPC Services
                                                     ↓
                                               Databases/External APIs
```

## Implementation Steps

### 1. Create GraphQL Service Structure
```bash
# Create GraphQL service directories
mkdir -p apollo-gateway
mkdir -p user-graphql-service
mkdir -p order-graphql-service

# GraphQL service package.json
cd user-graphql-service
npm init -y
npm install apollo-server-express @apollo/federation graphql
npm install grpc @grpc/grpc-js google-protobuf
```

### 2. Docker Compose with GraphQL
```yaml
# docker-compose.graphql.yml
version: '3.8'
services:
  # Existing services
  user-grpc-service:
    build: ./backend
    ports:
      - "9090:9090"
    networks:
      - service-mesh

  # New GraphQL services
  user-graphql-service:
    build: ./user-graphql-service
    ports:
      - "4001:4001"
    depends_on:
      - user-grpc-service
    environment:
      - GRPC_SERVICE_URL=user-grpc-service:9090
    networks:
      - service-mesh

  apollo-gateway:
    build: ./apollo-gateway
    ports:
      - "4000:4000"
    depends_on:
      - user-graphql-service
    networks:
      - service-mesh

  # Frontend with GraphQL client
  frontend:
    build: ./frontend-graphql
    ports:
      - "3000:3000"
    environment:
      - REACT_APP_GRAPHQL_URL=http://localhost:4000/graphql
    networks:
      - service-mesh

networks:
  service-mesh:
    driver: bridge
```

### 3. Kubernetes with Istio
```yaml
# k8s/apollo-gateway.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: apollo-gateway
  labels:
    app: apollo-gateway
spec:
  replicas: 3
  selector:
    matchLabels:
      app: apollo-gateway
  template:
    metadata:
      labels:
        app: apollo-gateway
      annotations:
        sidecar.istio.io/inject: "true"
    spec:
      containers:
      - name: apollo-gateway
        image: apollo-gateway:latest
        ports:
        - containerPort: 4000
        env:
        - name: USER_SERVICE_URL
          value: "http://user-graphql-service:4001/graphql"
        - name: ORDER_SERVICE_URL
          value: "http://order-graphql-service:4002/graphql"
```

## Frontend Evolution

### Current gRPC-Web Client
```typescript
// Keep for internal/admin tools
const grpcClient = new UserServiceClient('http://localhost:8080', null, null);
```

### New GraphQL Client
```typescript
// Apollo Client for main application
import { ApolloClient, InMemoryCache, gql } from '@apollo/client';

const client = new ApolloClient({
  uri: 'http://localhost:4000/graphql',
  cache: new InMemoryCache(),
});

const GET_USERS = gql`
  query GetUsers {
    users {
      id
      name
      email
      role
      createdAt
    }
  }
`;

const CREATE_USER = gql`
  mutation CreateUser($input: CreateUserInput!) {
    createUser(input: $input) {
      id
      name
      email
      role
    }
  }
`;
```

## Benefits of This Evolution

### For Development
1. **Best of Both Worlds**: GraphQL for clients, gRPC for services
2. **Gradual Migration**: Can migrate clients incrementally
3. **Type Safety**: End-to-end type safety maintained
4. **Performance**: gRPC efficiency for service-to-service communication

### For Operations
1. **Service Mesh**: Istio provides observability, security, traffic management
2. **Scalability**: Federation allows independent scaling of GraphQL services
3. **Resilience**: Circuit breaking, retries, timeouts
4. **Security**: mTLS, authentication, authorization policies

### For Clients
1. **Better DX**: Single GraphQL endpoint vs multiple gRPC services
2. **Flexibility**: Query exactly what you need
3. **Caching**: Apollo Client provides sophisticated caching
4. **Real-time**: GraphQL subscriptions for live updates

## Migration Timeline

### Week 1-2: Setup Foundation
- Create Apollo Gateway structure
- Set up first GraphQL service (Users)
- Implement gRPC-to-GraphQL mapping

### Week 3-4: Add More Services
- Create additional GraphQL services
- Implement Apollo Federation
- Set up federation gateway

### Week 5-6: Introduce Istio
- Deploy to Kubernetes
- Configure Istio service mesh
- Set up observability and security

### Week 7-8: Frontend Migration
- Create GraphQL client components
- Implement parallel GraphQL/gRPC-Web support
- Gradual client migration

## Conclusion

This evolution path allows you to:
1. **Keep existing gRPC services** as internal APIs
2. **Add GraphQL layer** for better client experience
3. **Introduce Istio** for service mesh capabilities
4. **Scale to multiple services** with Federation
5. **Maintain type safety** throughout the stack

The result is a modern, scalable, observable GraphQL service mesh built on solid gRPC foundations.

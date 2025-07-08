# Project Structure

This document provides a detailed overview of the project structure and organization.

## 📁 Root Directory Structure

```
stream-example/
├── .github/                    # GitHub configuration and workflows
│   ├── workflows/              # CI/CD pipeline definitions
│   ├── copilot-instructions/   # GitHub Copilot configuration
│   └── dependabot.yml         # Dependency management
├── backend/                    # Spring Boot gRPC Server (Java 24)
├── apollo-gateway/            # Apollo Federation Gateway (Node.js)
├── user-graphql-service/      # User GraphQL Service (Node.js)
├── frontend-graphql/          # React Frontend with Apollo Client
├── docs/                      # Documentation
├── docker-compose.graphql.yml # GraphQL federation stack
└── README.md                  # Project overview
```

## 🔧 Backend Directory (Spring Boot + gRPC)

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/jrg/grpc/
│   │   │       ├── GrpcApplication.java      # Main Spring Boot application
│   │   │       ├── service/
│   │   │       │   └── UserGrpcService.java  # gRPC service implementation
│   │   │       ├── model/
│   │   │       │   └── UserData.java         # Data models
│   │   │       └── config/
│   │   │           └── GrpcConfig.java       # gRPC configuration
│   │   ├── proto/
│   │   │   └── user_service.proto            # Protocol Buffer definitions
│   │   └── resources/
│   │       ├── application.properties        # Spring configuration
│   │       └── logback-spring.xml           # Logging configuration
│   └── test/
│       └── java/
│           └── org/jrg/grpc/
│               ├── GrpcApplicationTests.java
│               └── service/
│                   └── UserGrpcServiceTest.java
├── target/                     # Maven build output
│   ├── classes/               # Compiled Java classes
│   ├── generated-sources/     # Generated gRPC code
│   └── *.jar                  # Built application JAR
├── Dockerfile                 # Multi-stage Docker build
├── pom.xml                    # Maven dependencies and plugins
└── .gitignore                # Java-specific git ignores
```

### Key Backend Files

#### `user_service.proto`
```protobuf
syntax = "proto3";

package org.jrg.grpc;

service UserService {
  rpc GetUser(GetUserRequest) returns (User);
  rpc GetAllUsers(GetAllUsersRequest) returns (GetAllUsersResponse);
  rpc CreateUser(CreateUserRequest) returns (User);
  rpc UpdateUser(UpdateUserRequest) returns (User);
  rpc DeleteUser(DeleteUserRequest) returns (DeleteUserResponse);
}

message User {
  int64 id = 1;
  string name = 2;
  string email = 3;
  string role = 4;
  string created_at = 5;
}
```

#### `UserGrpcService.java`
- Main gRPC service implementation
- Uses `@GrpcService` annotation
- Implements all Protocol Buffer service methods
- Provides proper error handling with gRPC status codes

## 🌐 Apollo Gateway Directory

```
apollo-gateway/
├── src/
│   ├── index.js               # Main gateway server
│   ├── config/
│   │   └── gateway.js         # Gateway configuration
│   └── health/
│       └── healthCheck.js     # Health check endpoint
├── package.json               # Node.js dependencies
├── Dockerfile                 # Gateway container
├── .env.example              # Environment variables template
└── README.md                 # Gateway-specific documentation
```

### Key Gateway Files

#### `index.js`
```javascript
const { ApolloGateway } = require('@apollo/gateway');
const { ApolloServer } = require('apollo-server-express');

const gateway = new ApolloGateway({
  supergraphSdl: new IntrospectAndCompose({
    subgraphs: [
      { name: 'users', url: 'http://user-graphql-service:4001/graphql' },
    ],
  }),
});

const server = new ApolloServer({
  gateway,
  subscriptions: false,
});
```

## 📊 User GraphQL Service Directory

```
user-graphql-service/
├── src/
│   ├── index.js               # Main service server
│   ├── schema/
│   │   ├── typeDefs.js        # GraphQL type definitions
│   │   └── resolvers.js       # GraphQL resolvers
│   ├── grpc/
│   │   ├── client.js          # gRPC client configuration
│   │   └── generated/         # Generated gRPC code (from proto)
│   ├── utils/
│   │   ├── logger.js          # Logging utilities
│   │   └── errorHandler.js    # Error handling
│   └── health/
│       └── healthCheck.js     # Health check endpoint
├── package.json               # Node.js dependencies
├── Dockerfile                 # Service container
├── .env.example              # Environment variables template
└── README.md                 # Service-specific documentation
```

### Key GraphQL Service Files

#### `typeDefs.js`
```javascript
const { gql } = require('apollo-server-express');

const typeDefs = gql`
  type User @key(fields: "id") {
    id: ID!
    name: String!
    email: String!
    role: UserRole!
    createdAt: String!
  }

  enum UserRole {
    ADMIN
    USER
  }

  type Query {
    users: [User!]!
    user(id: ID!): User
  }

  type Mutation {
    createUser(input: CreateUserInput!): User!
    updateUser(id: ID!, input: UpdateUserInput!): User!
    deleteUser(id: ID!): Boolean!
  }
`;
```

#### `resolvers.js`
- Maps GraphQL operations to gRPC calls
- Handles data transformation between GraphQL and gRPC
- Implements federation directives
- Provides proper error handling

## 🎨 Frontend Directory (React + Apollo Client)

```
frontend-graphql/
├── public/
│   ├── index.html             # HTML template
│   └── favicon.ico           # Application icon
├── src/
│   ├── components/
│   │   ├── UserList.jsx       # User listing component
│   │   ├── UserForm.jsx       # User creation/editing form
│   │   ├── UserCard.jsx       # Individual user display
│   │   └── common/
│   │       ├── Loading.jsx    # Loading component
│   │       └── ErrorMessage.jsx # Error display component
│   ├── graphql/
│   │   ├── queries.js         # GraphQL query definitions
│   │   ├── mutations.js       # GraphQL mutation definitions
│   │   └── client.js          # Apollo Client configuration
│   ├── hooks/
│   │   ├── useUsers.js        # Custom hook for user operations
│   │   └── useErrorHandler.js # Error handling hook
│   ├── utils/
│   │   ├── constants.js       # Application constants
│   │   └── helpers.js         # Utility functions
│   ├── styles/
│   │   ├── App.css           # Main application styles
│   │   └── components/       # Component-specific styles
│   ├── App.jsx               # Main application component
│   ├── index.js              # Application entry point
│   └── setupTests.js         # Test configuration
├── package.json              # Dependencies and scripts
├── vite.config.js            # Vite configuration
├── Dockerfile                # Frontend container
└── README.md                # Frontend-specific documentation
```

### Key Frontend Files

#### `client.js` (Apollo Client Setup)
```javascript
import { ApolloClient, InMemoryCache, ApolloProvider } from '@apollo/client';

const client = new ApolloClient({
  uri: 'http://localhost:4000/graphql',
  cache: new InMemoryCache(),
  defaultOptions: {
    watchQuery: {
      errorPolicy: 'all',
    },
  },
});
```

#### `queries.js`
```javascript
import { gql } from '@apollo/client';

export const GET_USERS = gql`
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

export const GET_USER = gql`
  query GetUser($id: ID!) {
    user(id: $id) {
      id
      name
      email
      role
      createdAt
    }
  }
`;
```

## 📚 Documentation Directory

```
docs/
├── README.md                 # Documentation index
├── INSTRUCTIONS.md           # Architecture and setup guide
├── DEVELOPMENT.md            # Development workflow guide
├── FEATURES.md              # Feature documentation
├── PROJECT_STRUCTURE.md     # This file
├── DEPLOYMENT.md            # Deployment guide
├── TROUBLESHOOTING.md       # Common issues and solutions
├── GRAPHQL_API.md           # GraphQL API reference
├── ADDING_APOLLO.md         # Migration documentation
└── CLEANUP_REPORT.md        # Cleanup and modernization report
```

## 🔄 CI/CD Directory

```
.github/
├── workflows/
│   ├── ci-cd.yml            # Main CI/CD pipeline
│   └── README.md            # Pipeline documentation
├── copilot-instructions/
│   ├── java.md             # Java development guidelines
│   ├── javascript.md       # JavaScript/TypeScript guidelines
│   └── README.md           # Copilot configuration overview
└── copilot-instructions.md  # Main Copilot instructions
```

## 🐳 Docker Configuration

### Root Level Files
- `docker-compose.graphql.yml` - Complete GraphQL federation stack
- Each service has its own `Dockerfile` for containerization

### Service-Specific Dockerfiles
- **Backend**: Multi-stage build with Maven and JRE
- **Apollo Gateway**: Node.js with Alpine Linux
- **GraphQL Service**: Node.js with production optimizations
- **Frontend**: Vite build with Nginx serving

## 📦 Package Management

### Backend (Maven)
- `pom.xml` - Dependencies, plugins, and build configuration
- Uses Spring Boot parent for dependency management
- Includes gRPC, Spring gRPC, and Protocol Buffers dependencies

### Node.js Services
- `package.json` - Dependencies and scripts for each service
- Uses npm for package management
- Includes Apollo Federation, GraphQL, and related dependencies

### Frontend
- `package.json` - React, Apollo Client, and build tools
- Uses Vite for development and building
- Includes TypeScript support and testing libraries

## 🏗️ Build Artifacts

### Backend Build Outputs
```
backend/target/
├── classes/                  # Compiled Java classes
├── generated-sources/        # Generated Protocol Buffer classes
│   └── protobuf/
│       ├── java/            # Java gRPC stubs
│       └── grpc-java/       # gRPC service implementations
├── test-classes/            # Compiled test classes
└── *.jar                    # Executable JAR files
```

### Frontend Build Outputs
```
frontend-graphql/dist/
├── index.html               # Built HTML
├── assets/                  # Bundled CSS/JS
│   ├── index.*.js          # Application bundle
│   └── index.*.css         # Styles bundle
└── static/                  # Static assets
```

## 🔧 Configuration Files

### Environment Configuration
- `.env.example` files in each service for environment variable templates
- `application.properties` for Spring Boot configuration
- Service-specific configuration in each directory

### Development Tools
- `.gitignore` files for appropriate exclusions
- Editor configuration files (VS Code, IntelliJ)
- Linting and formatting configuration

This structure provides clear separation of concerns, making it easy to:
- Develop and test services independently
- Scale individual components
- Maintain clear service boundaries
- Deploy services separately or together
- Add new services to the federation

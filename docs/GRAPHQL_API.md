# GraphQL API Documentation

This document describes the GraphQL API that wraps your existing gRPC services using Apollo Federation.

## Architecture

```
Frontend (GraphQL) → Apollo Gateway → User GraphQL Service → User gRPC Service
```

## Endpoints

- **Apollo Gateway**: http://localhost:4000/graphql
- **User GraphQL Service**: http://localhost:4001/graphql
- **GraphQL Playground**: http://localhost:4000/graphql (in development)

## Schema

### Types

#### User
```graphql
type User {
  id: ID!
  name: String!
  email: String!
  role: UserRole!
  createdAt: String!
}
```

#### UserRole
```graphql
enum UserRole {
  USER
  ADMIN
  MODERATOR
}
```

### Inputs

#### CreateUserInput
```graphql
input CreateUserInput {
  name: String!
  email: String!
  role: UserRole!
}
```

#### UpdateUserInput
```graphql
input UpdateUserInput {
  name: String
  email: String
  role: UserRole
}
```

## Operations

### Queries

#### Get All Users
```graphql
query GetUsers {
  users {
    id
    name
    email
    role
    createdAt
  }
}
```

#### Get Single User
```graphql
query GetUser($id: ID!) {
  user(id: $id) {
    id
    name
    email
    role
    createdAt
  }
}
```

### Mutations

#### Create User
```graphql
mutation CreateUser($input: CreateUserInput!) {
  createUser(input: $input) {
    id
    name
    email
    role
    createdAt
  }
}
```

**Variables:**
```json
{
  "input": {
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

#### Update User
```graphql
mutation UpdateUser($id: ID!, $input: UpdateUserInput!) {
  updateUser(id: $id, input: $input) {
    id
    name
    email
    role
    createdAt
  }
}
```

**Variables:**
```json
{
  "id": "1",
  "input": {
    "name": "John Smith",
    "email": "john.smith@example.com"
  }
}
```

#### Delete User
```graphql
mutation DeleteUser($id: ID!) {
  deleteUser(id: $id)
}
```

**Variables:**
```json
{
  "id": "1"
}
```

## Example Usage with Apollo Client

### Setup Apollo Client
```typescript
import { ApolloClient, InMemoryCache } from '@apollo/client';

const client = new ApolloClient({
  uri: 'http://localhost:4000/graphql',
  cache: new InMemoryCache(),
});
```

### React Component with Hooks
```typescript
import { useQuery, useMutation, gql } from '@apollo/client';

const GET_USERS = gql`
  query GetUsers {
    users {
      id
      name
      email
      role
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

function UserList() {
  const { data, loading, error } = useQuery(GET_USERS);
  const [createUser] = useMutation(CREATE_USER, {
    refetchQueries: [{ query: GET_USERS }],
  });

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <div>
      {data.users.map(user => (
        <div key={user.id}>
          {user.name} ({user.email}) - {user.role}
        </div>
      ))}
    </div>
  );
}
```

## Error Handling

GraphQL errors are returned in the standard GraphQL error format:

```json
{
  "errors": [
    {
      "message": "User not found",
      "locations": [{ "line": 2, "column": 3 }],
      "path": ["user"]
    }
  ],
  "data": {
    "user": null
  }
}
```

## gRPC Integration

The GraphQL service acts as a wrapper around the existing gRPC service:

1. **GraphQL Request** → Apollo Gateway
2. **Route to Service** → User GraphQL Service  
3. **gRPC Call** → User gRPC Service (localhost:9090)
4. **gRPC Response** → User GraphQL Service
5. **GraphQL Response** → Apollo Gateway
6. **Final Response** → Client

## Federation Features

With Apollo Federation, you can:

1. **Add More Services**: Create additional GraphQL services for Orders, Products, etc.
2. **Cross-Service Queries**: Query data across multiple services in a single request
3. **Service Independence**: Each service can be developed and deployed independently
4. **Schema Composition**: Gateway automatically composes schemas from all services

## Development Tools

### GraphQL Playground
Visit http://localhost:4000/graphql to access the interactive GraphQL playground where you can:
- Explore the schema
- Run queries and mutations
- View documentation
- Test the API

### Health Checks
- Apollo Gateway: http://localhost:4000/health
- User GraphQL Service: http://localhost:4001/health

### Logs
```bash
# View all service logs
docker-compose -f docker-compose.graphql.yml logs

# View specific service logs
docker-compose -f docker-compose.graphql.yml logs apollo-gateway
docker-compose -f docker-compose.graphql.yml logs user-graphql-service
```

## Migration from gRPC-Web

If you're migrating from the existing gRPC-Web frontend:

1. **Install Apollo Client**: `npm install @apollo/client graphql`
2. **Replace gRPC-Web imports** with Apollo Client imports
3. **Convert gRPC calls** to GraphQL queries/mutations
4. **Update state management** to use Apollo Cache
5. **Replace error handling** with GraphQL error handling

Both systems can run in parallel during migration!

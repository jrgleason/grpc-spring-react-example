# GitHub Copilot Instructions for JavaScript/TypeScript Frontend

## Project Context
This is a React TypeScript frontend that communicates with a GraphQL Federation Gateway via Apollo Client. The application uses **GraphQL over HTTP** - no direct gRPC calls from the frontend.

## Architecture Overview
- **Frontend**: React 18+ with TypeScript and Apollo Client (Port 3000)
- **API Gateway**: Apollo Federation Gateway (Port 4000)
- **GraphQL Services**: Node.js services that wrap gRPC backend calls (Port 4001+)
- **Backend**: Spring Boot gRPC server (Port 9090)
- **Protocol**: GraphQL over HTTP, with gRPC communication handled by GraphQL services

## Key Technologies & Dependencies
- React 18.2+ with TypeScript 4.9+
- Apollo Client 3.8+ (`@apollo/client`)
- GraphQL (`graphql`)
- Create React App with TypeScript template

## Apollo Client Implementation Patterns

### Client Initialization
Always initialize Apollo Client to connect to the GraphQL Federation Gateway:
```typescript
import { ApolloClient, InMemoryCache, ApolloProvider } from '@apollo/client';

const client = new ApolloClient({
  uri: 'http://localhost:4000/graphql',
  cache: new InMemoryCache(),
});
```

### Query Implementation
Use Apollo hooks for GraphQL queries:
```typescript
import { gql, useQuery } from '@apollo/client';

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

const UsersList = () => {
  const { data, loading, error } = useQuery(GET_USERS);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <div>
      {data.users.map(user => (
        <div key={user.id}>{user.name}</div>
      ))}
    </div>
  );
};
```

### Mutation Implementation
Use Apollo hooks for GraphQL mutations:
```typescript
import { gql, useMutation } from '@apollo/client';

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

const CreateUserForm = () => {
  const [createUser, { loading, error }] = useMutation(CREATE_USER, {
    refetchQueries: ['GetUsers'], // Refresh user list after creation
  });

  const handleSubmit = async (formData) => {
    try {
      await createUser({
        variables: {
          input: {
            name: formData.name,
            email: formData.email,
            role: formData.role,
          }
        }
      });
    } catch (error) {
      console.error('Error creating user:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      {/* Form fields */}
    </form>
  );
};
```

## GraphQL Data Handling

### Error Handling Pattern
Handle GraphQL errors with proper user feedback:
```typescript
import { ApolloError } from '@apollo/client';

const handleError = (error: ApolloError) => {
  if (error.networkError) {
    console.error('Network error:', error.networkError);
    return 'Network error occurred';
  }
  
  if (error.graphQLErrors?.length > 0) {
    console.error('GraphQL errors:', error.graphQLErrors);
    return error.graphQLErrors[0].message;
  }
  
  return 'An unexpected error occurred';
};
```

### Cache Management
Leverage Apollo Client's intelligent caching:
```typescript
// Optimistic updates for better UX
const [updateUser] = useMutation(UPDATE_USER, {
  optimisticResponse: {
    updateUser: {
      __typename: 'User',
      id: userId,
      name: newName,
      email: newEmail,
      role: newRole,
    },
  },
  update: (cache, { data }) => {
    // Update cache manually if needed
    cache.modify({
      id: cache.identify(data.updateUser),
      fields: {
        name: () => data.updateUser.name,
      },
    });
  },
});
```

## React Component Patterns

### State Management with Apollo
```typescript
interface User {
  id: string;
  name: string;
  email: string;
  role: string;
  createdAt: string;
}

const UserManagement = () => {
  const { data, loading, error, refetch } = useQuery<{users: User[]}>(GET_USERS);
  const [createUser] = useMutation(CREATE_USER);
  const [updateUser] = useMutation(UPDATE_USER);
  const [deleteUser] = useMutation(DELETE_USER);

  // Component logic using Apollo hooks
  return (
    <div>
      {/* Component JSX */}
    </div>
  );
};
```

### Loading and Error States
```typescript
const UsersList = () => {
  const { data, loading, error } = useQuery(GET_USERS);

  if (loading) return <LoadingSpinner />;
  if (error) return <ErrorMessage error={error} />;
  if (!data?.users?.length) return <EmptyState />;

  return (
    <div>
      {data.users.map(user => (
        <UserCard key={user.id} user={user} />
      ))}
    </div>
  );
};
```

## Development Workflow

### Schema Changes Process
1. GraphQL services automatically update their schemas
2. Apollo Gateway recomposes the federated schema
3. Frontend automatically gets new schema via introspection
4. Update React components to use new fields/operations

### Code Generation (Optional)
```json
{
  "scripts": {
    "codegen": "graphql-codegen --config codegen.yml",
    "type-check": "tsc --noEmit"
  }
}
```

### Testing GraphQL Integration
- Use Apollo Client's MockedProvider for component testing
- Test GraphQL operations with GraphQL Playground at http://localhost:4000/graphql
- Use Apollo Client DevTools browser extension for debugging

## Best Practices

### Code Organization
- Keep GraphQL queries/mutations in separate files
- Use fragments for reusable field selections
- Implement proper TypeScript types for GraphQL operations
- Create custom hooks for complex GraphQL logic

### Performance Considerations
- Use Apollo Client's built-in caching effectively
- Implement proper pagination for large datasets
- Use Apollo's `fetchPolicy` options appropriately
- Consider using subscriptions for real-time updates

### Error Handling
- Implement consistent error handling across components
- Use Apollo's error boundaries for global error handling
- Provide meaningful user feedback for different error types
- Log errors appropriately for debugging

## TypeScript Configuration

### GraphQL Types
Generate TypeScript types from GraphQL schema:
```typescript
// Generated types
export interface User {
  __typename?: 'User';
  id: string;
  name: string;
  email: string;
  role: UserRole;
  createdAt: string;
}

export enum UserRole {
  ADMIN = 'ADMIN',
  USER = 'USER'
}
```

### Query/Mutation Types
Use properly typed Apollo hooks:
```typescript
import { DocumentNode } from 'graphql';

const GET_USERS: DocumentNode = gql`
  query GetUsers {
    users {
      id
      name
      email
      role
    }
  }
`;

// TypeScript will infer types from the query
const { data, loading, error } = useQuery(GET_USERS);
```

## Common Patterns

### Reusable GraphQL Hooks
```typescript
export const useUsers = () => {
  const { data, loading, error, refetch } = useQuery(GET_USERS);
  
  return {
    users: data?.users || [],
    loading,
    error,
    refetch,
  };
};

export const useCreateUser = () => {
  const [createUserMutation, { loading, error }] = useMutation(CREATE_USER);
  
  const createUser = async (input: CreateUserInput) => {
    const { data } = await createUserMutation({
      variables: { input },
      refetchQueries: ['GetUsers'],
    });
    return data?.createUser;
  };
  
  return { createUser, loading, error };
};
```

### Fragment Usage
```typescript
const USER_FRAGMENT = gql`
  fragment UserFields on User {
    id
    name
    email
    role
    createdAt
  }
`;

const GET_USERS = gql`
  ${USER_FRAGMENT}
  query GetUsers {
    users {
      ...UserFields
    }
  }
`;
```

## Troubleshooting Guidelines

### Common Issues
- **Network errors**: Check if Apollo Gateway is running on port 4000
- **Schema errors**: Verify GraphQL services are running and federated properly
- **Type errors**: Ensure GraphQL operations match the available schema
- **Cache issues**: Use Apollo Client DevTools to inspect cache state

### Debugging Tips
- Use GraphQL Playground to test queries before implementing
- Check browser Network tab for GraphQL requests
- Use Apollo Client DevTools browser extension
- Enable Apollo Client's developer mode for better error messages

## Integration Points
- **API Gateway**: Communicates with Apollo Federation Gateway (localhost:4000/graphql)
- **Development**: Hot reload with Create React App
- **Testing**: Unit tests with React Testing Library and Apollo MockedProvider
- **Build**: Builds to static files for deployment

## Styling and UI
- Use modern CSS-in-JS or CSS modules for styling
- Implement responsive design for mobile compatibility
- Create reusable components for GraphQL data display
- Handle loading and error states with proper UI feedback
- Use Apollo Client's loading states for better UX

# GitHub Copilot Instructions for JavaScript/TypeScript Frontend

## Project Context
This is a React TypeScript frontend that communicates exclusively with a gRPC backend via gRPC-Web protocol. The application uses **NO REST API calls** - all server communication is through gRPC-Web.

## Architecture Overview
- **Frontend**: React 18+ with TypeScript and gRPC-Web client (Port 3000)
- **Proxy**: Envoy proxy for gRPC-Web translation (Port 8080)
- **Backend**: Spring Boot gRPC server (Port 9090)
- **Protocol**: 100% gRPC-Web communication using Protocol Buffers

## Key Technologies & Dependencies
- React 18.2+ with TypeScript 4.9+
- gRPC-Web 1.4+ (`grpc-web`)
- Google Protocol Buffers (`google-protobuf`)
- Create React App with TypeScript template
- Auto-generated TypeScript clients from .proto files

## Code Generation & Build Process
- **Proto Files**: Located in `backend/src/main/proto/`
- **Generated Code**: TypeScript clients in `frontend/src/generated/`
- **Generation Script**: `npm run proto:generate` (runs protoc with gRPC-Web plugin)
- **Workflow**: Proto changes → regenerate → update React components

## gRPC-Web Client Implementation Patterns

### Client Initialization
Always initialize the gRPC client to connect through Envoy proxy:
```typescript
import { UserServiceClient } from './generated/User_serviceServiceClientPb';

const grpcClient = new UserServiceClient('http://localhost:8080', null, null);
```

### Promise-based gRPC Calls
Convert gRPC callbacks to Promises for better async handling:
```typescript
const loadUsers = async () => {
  const request = new GetAllUsersRequest();
  
  const response = await new Promise<GetAllUsersResponse>((resolve, reject) => {
    grpcClient.getAllUsers(request, {}, (err: any, response: any) => {
      if (err) {
        reject(err);
      } else {
        resolve(response);
      }
    });
  });
  
  return response.getUsersList();
};
```

### Error Handling Pattern
Handle gRPC errors with proper status codes:
```typescript
try {
  const response = await grpcCall();
  // Handle success
} catch (error: any) {
  if (error.code === 5) { // NOT_FOUND
    setError('User not found');
  } else if (error.code === 3) { // INVALID_ARGUMENT
    setError('Invalid input data');
  } else {
    setError('Server error occurred');
  }
}
```

## Protocol Buffer Data Handling

### Converting Proto Objects to JavaScript
```typescript
// Proto to JavaScript object conversion
const usersData: UserData[] = usersList.map((user: User) => ({
  id: user.getId(),
  name: user.getName(),
  email: user.getEmail(),
  role: user.getRole(),
  createdAt: user.getCreatedAt()
}));
```

### Creating Proto Messages for Requests
```typescript
// Create request messages using builders
const request = new CreateUserRequest();
request.setName(formData.name);
request.setEmail(formData.email);
request.setRole(formData.role);

// Send the request
const createdUser = await grpcClient.createUser(request, {});
```

### Working with Repeated Fields
```typescript
// Handle repeated fields (arrays) in proto messages
const usersList = response.getUsersList(); // Returns User[]
const userIds = response.getUserIdsList(); // Returns number[]
```

## React Component Patterns

### State Management for gRPC Data
```typescript
interface UserData {
  id: number;
  name: string;
  email: string;
  role: string;
  createdAt: string;
}

const [users, setUsers] = useState<UserData[]>([]);
const [loading, setLoading] = useState(false);
const [error, setError] = useState<string | null>(null);
```

### useEffect for Data Loading
```typescript
useEffect(() => {
  const loadData = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await grpcCall();
      setUsers(data);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };
  
  loadData();
}, []);
```

### Form Handling with gRPC
```typescript
const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  
  try {
    const request = new CreateUserRequest();
    request.setName(formData.name);
    request.setEmail(formData.email);
    request.setRole(formData.role);
    
    await grpcClient.createUser(request, {});
    
    // Reload data and reset form
    await loadUsers();
    setFormData({ name: '', email: '', role: 'USER' });
    setShowForm(false);
  } catch (error: any) {
    setError(error.message);
  }
};
```

## Generated Code Structure

### Import Patterns
```typescript
// Service client import
import { UserServiceClient } from './generated/User_serviceServiceClientPb';

// Message types import
import { 
  User, 
  GetAllUsersRequest, 
  CreateUserRequest, 
  UpdateUserRequest 
} from './generated/user_service_pb';
```

### Generated File Structure
- `*ServiceClientPb.ts` - gRPC service client
- `*_pb.ts` - Protocol buffer message classes
- `*_pb.d.ts` - TypeScript type definitions

## Development Workflow

### Proto Changes Process
1. Modify `.proto` files in `backend/src/main/proto/`
2. Run `npm run proto:generate` in frontend directory
3. Update React components to match new proto definitions
4. Test with backend to ensure compatibility

### Code Generation Script
```json
{
  "scripts": {
    "proto:generate": "protoc --plugin=protoc-gen-grpc-web=./node_modules/.bin/protoc-gen-grpc-web --js_out=import_style=commonjs:./src/generated --grpc-web_out=import_style=typescript,mode=grpcwebtext:./src/generated ../backend/src/main/proto/*.proto"
  }
}
```

### Testing gRPC-Web Integration
- Ensure backend gRPC server is running (port 9090)
- Ensure Envoy proxy is running (port 8080)
- Use browser dev tools to monitor network requests to Envoy
- Check for CORS errors in browser console

## Best Practices

### Code Organization
- Keep generated files in `src/generated/` directory
- Create service wrapper functions for complex gRPC calls
- Use TypeScript interfaces for component props and state
- Implement proper error boundaries for gRPC errors

### Performance Considerations
- Implement request cancellation for component unmounting
- Use React.memo for expensive rendering operations
- Cache gRPC client instances
- Implement proper loading states

### Error Handling
- Map gRPC status codes to user-friendly messages
- Implement retry logic for transient failures
- Show appropriate loading and error states
- Log errors for debugging (but not sensitive data)

## TypeScript Configuration

### Strict Type Checking
Always use strict TypeScript settings for better type safety:
```json
{
  "compilerOptions": {
    "strict": true,
    "noImplicitAny": true,
    "strictNullChecks": true
  }
}
```

### Proto Generated Types
Work with generated protobuf types:
```typescript
// Use generated types for type safety
const user: User = new User();
user.setName('John Doe');

// Convert to plain objects when needed
const userData: UserData = {
  id: user.getId(),
  name: user.getName(),
  // ... other fields
};
```

## Common Patterns

### Async/Await with gRPC-Web
```typescript
const grpcCall = async <T>(
  call: (callback: (err: any, response: T) => void) => void
): Promise<T> => {
  return new Promise((resolve, reject) => {
    call((err: any, response: T) => {
      if (err) reject(err);
      else resolve(response);
    });
  });
};
```

### Loading State Management
```typescript
const useGrpcCall = <T>(grpcFunction: () => Promise<T>) => {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  const execute = async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await grpcFunction();
      setData(result);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };
  
  return { data, loading, error, execute };
};
```

## Troubleshooting Guidelines

### Common Issues
- **CORS errors**: Check Envoy configuration and frontend URL
- **Connection refused**: Verify Envoy proxy is running on port 8080
- **Method not found**: Regenerate client code after proto changes
- **Type errors**: Ensure generated TypeScript files are up to date

### Debugging Tips
- Use browser Network tab to inspect gRPC-Web requests
- Check console for gRPC error messages and status codes
- Verify proto file synchronization between frontend and backend
- Test backend gRPC service independently with grpcurl

## Integration Points
- **Backend**: Communicates via Envoy proxy (localhost:8080)
- **Build Process**: Integrates with protoc for code generation
- **Development**: Hot reload with Create React App
- **Testing**: Unit tests with React Testing Library and gRPC mocking

## Styling and UI
- Use modern CSS-in-JS or CSS modules for styling
- Implement responsive design for mobile compatibility
- Create reusable components for gRPC data display
- Handle loading and error states with proper UI feedback

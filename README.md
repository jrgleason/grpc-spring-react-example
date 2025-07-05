# React + Spring Boot gRPC Demo

This project demonstrates communication between a React frontend and a Spring Boot backend using gRPC with gRPC-Web.

## Project Structure

```
stream-example/
├── backend/          # Spring Boot gRPC backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   ├── proto/    # Protocol Buffer definitions
│   │   │   └── resources/
│   │   ├── test/
│   │   └── Dockerfile
│   └── pom.xml
├── frontend/         # React TypeScript frontend
│   ├── src/
│   │   ├── generated/    # Generated gRPC-Web client code
│   │   ├── App.tsx
│   │   └── ...
│   ├── public/
│   ├── package.json
│   └── tsconfig.json
├── envoy.yaml        # Envoy proxy configuration for gRPC-Web
└── docker-compose.yml
```

## Features

### Backend (Spring Boot + gRPC)
- Spring Boot 3.5.4-SNAPSHOT with official Spring gRPC support
- Protocol Buffers for service definitions
- UserService with CRUD operations
- gRPC server on port 9090
- No REST endpoints (gRPC-only)

### Frontend (React + TypeScript + gRPC-Web)
- Modern React 18 with TypeScript
- Responsive UI with CSS Grid and Flexbox
- gRPC-Web client integration
- User management with CRUD operations
- Real-time communication via gRPC

### Envoy Proxy
- gRPC-Web proxy for browser compatibility
- CORS support for development
- HTTP/2 to gRPC translation

## Getting Started

### Prerequisites
- Java 24+
- Node.js 18+
- Maven 3.9+
- Docker and Docker Compose

### Option 1: Docker Compose (Recommended)

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

2. Compile and generate protobuf classes:
   ```bash
   mvn clean compile
   ```

3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

The backend will start on port 9090 (gRPC only).

#### Envoy Proxy Setup

1. Install Docker if not already installed

2. Run Envoy proxy:
   ```bash
   docker run -it --rm -p 8080:8080 -p 9901:9901 \
     -v $(pwd)/envoy.yaml:/etc/envoy/envoy.yaml \
     envoyproxy/envoy:v1.28-latest \
     -c /etc/envoy/envoy.yaml -l debug
   ```

#### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Generate gRPC-Web client code:
   ```bash
   npm run proto:generate
   ```

4. Start the development server:
   ```bash
   npm start
   ```

The frontend will start on http://localhost:3000

## API Endpoints

### gRPC Service (via gRPC-Web on port 8080)
- `GetUser(GetUserRequest)` - Get user by ID
- `GetAllUsers(GetAllUsersRequest)` - Get all users
- `CreateUser(CreateUserRequest)` - Create new user
- `UpdateUser(UpdateUserRequest)` - Update user
- `DeleteUser(DeleteUserRequest)` - Delete user
- `StreamUsers(StreamUsersRequest)` - Stream users (server streaming)

All gRPC calls are made through the Envoy proxy at http://localhost:8080

## Protocol Buffer Definition

The `user_service.proto` file defines:
- `User` message with id, name, email, role, and creation timestamp
- Request/response messages for all operations
- `UserService` with unary and streaming RPC methods

## Usage

1. Start the services using Docker Compose or manually
2. Open http://localhost:3000 in your browser
3. The application will automatically connect to the gRPC backend via Envoy proxy
4. Use the UI to manage users with full CRUD operations

### Sample Operations
- **View Users**: The application loads with sample users via gRPC
- **Add User**: Click "Add User" to create a new user via gRPC
- **Edit User**: Click "Edit" on any user card to update via gRPC
- **Delete User**: Click "Delete" on any user card to remove via gRPC
- **Refresh**: Click "Refresh" to reload users from the gRPC server

## Technology Stack

### Backend
- **Spring Boot 3.5.4-SNAPSHOT**: Latest Spring framework
- **Spring gRPC 0.8.0**: Official Spring gRPC integration
- **Protocol Buffers 4.30.2**: Message serialization
- **gRPC 1.72.0**: High-performance RPC framework
- **Maven**: Build and dependency management

### Frontend
- **React 18**: Modern React with functional components
- **TypeScript**: Type-safe JavaScript
- **gRPC-Web**: Direct gRPC communication from browser
- **CSS3**: Modern styling with Grid and Flexbox
- **Protobuf**: Generated TypeScript client code

## Development Notes

### Backend Compilation
The project uses the `io.github.ascopes:protobuf-maven-plugin` to generate Java classes from `.proto` files. Generated classes are placed in `target/generated-sources/protobuf/`.

### Frontend gRPC Client Generation
The frontend uses `protoc` with `protoc-gen-grpc-web` to generate TypeScript client code from `.proto` files. Generated files are placed in `src/generated/`.

### Envoy Proxy Configuration
Envoy acts as a translation layer between the browser and gRPC backend:
- Receives gRPC-Web requests on port 8080
- Translates them to standard gRPC calls to backend on port 9090
- Handles CORS and HTTP/2 requirements

### Spring gRPC Configuration
The backend uses the official Spring gRPC support with:
- `@GrpcService` annotation for service implementation
- Automatic server configuration via Spring Boot starter
- Integration with Spring's dependency injection

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

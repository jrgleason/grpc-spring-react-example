#!/bin/bash

# Apollo GraphQL Federation Setup Script
echo "🚀 Setting up Apollo GraphQL Federation with gRPC Services"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    print_error "Docker is not running. Please start Docker and try again."
    exit 1
fi

print_status "Docker is running ✅"

# Check if Docker Compose is available
if ! command -v docker-compose &> /dev/null; then
    print_error "docker-compose is not installed. Please install Docker Compose and try again."
    exit 1
fi

print_status "Docker Compose is available ✅"

# Build all services
print_status "Building all services..."

print_status "Building gRPC Backend..."
cd backend && mvn clean package -DskipTests && cd ..
if [ $? -eq 0 ]; then
    print_success "gRPC Backend built successfully"
else
    print_error "Failed to build gRPC Backend"
    exit 1
fi

print_status "Installing Apollo Gateway dependencies..."
cd apollo-gateway && npm install && cd ..
if [ $? -eq 0 ]; then
    print_success "Apollo Gateway dependencies installed"
else
    print_error "Failed to install Apollo Gateway dependencies"
    exit 1
fi

print_status "Installing User GraphQL Service dependencies..."
cd user-graphql-service && npm install && cd ..
if [ $? -eq 0 ]; then
    print_success "User GraphQL Service dependencies installed"
else
    print_error "Failed to install User GraphQL Service dependencies"
    exit 1
fi

print_status "Installing Frontend GraphQL dependencies..."
cd frontend-graphql && npm install && cd ..
if [ $? -eq 0 ]; then
    print_success "Frontend GraphQL dependencies installed"
else
    print_error "Failed to install Frontend GraphQL dependencies"
    exit 1
fi

# Start services with Docker Compose
print_status "Starting all services with Docker Compose..."
docker-compose -f docker-compose.graphql.yml up --build -d

if [ $? -eq 0 ]; then
    print_success "All services started successfully!"
    
    echo ""
    print_status "🌟 Services are now running:"
    echo ""
    echo -e "${GREEN}📊 gRPC Backend:${NC}          grpc://localhost:9090"
    echo -e "${GREEN}🔗 Apollo Gateway:${NC}       http://localhost:4000/graphql"
    echo -e "${GREEN}📋 User GraphQL Service:${NC} http://localhost:4001/graphql"
    echo -e "${GREEN}🎨 Frontend (GraphQL):${NC}   http://localhost:3000"
    echo ""
    echo -e "${YELLOW}🔧 Admin Endpoints:${NC}"
    echo -e "${BLUE}Health Checks:${NC}           http://localhost:4000/health, http://localhost:4001/health"
    echo ""
    print_status "💡 Try the GraphQL Playground at: http://localhost:4000/graphql"
    print_status "💡 Try the Frontend at: http://localhost:3000"
    echo ""
    print_warning "To stop all services, run: docker-compose -f docker-compose.graphql.yml down"
    
else
    print_error "Failed to start services"
    exit 1
fi

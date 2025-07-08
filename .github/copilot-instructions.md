# GitHub Copilot Instructions for gRPC-Web Full-Stack Project

## Project Overview
This is a full-stack gRPC-Web application with a React TypeScript frontend and Spring Boot Java backend. **All communication is via gRPC protocol only - no REST endpoints.**

## Architecture
- **Frontend**: React + TypeScript + Apollo Client (Port 3000)
- **API Gateway**: Apollo Federation Gateway (Port 4000)  
- **GraphQL Services**: Node.js services wrapping gRPC calls (Port 4001+)
- **Backend**: Spring Boot + gRPC Server (Port 9090)

## Language-Specific Instructions

### For Java Backend Development
See [java.md](./copilot-instructions/java.md) for comprehensive Spring Boot gRPC service implementation patterns, configuration, and best practices.

### For JavaScript/TypeScript Frontend Development  
See [javascript.md](./copilot-instructions/javascript.md) for React gRPC-Web client patterns, proto code generation, and TypeScript integration.

## General Guidelines

We use Apollo GraphQL Federation for all frontend-backend communication - never suggest gRPC-Web patterns or Envoy proxy configurations. Always work with GraphQL schemas, resolvers, and Apollo Client patterns.

When suggesting architectural changes, consider the GraphQL Federation requirements and ensure compatibility between Apollo Client, Apollo Gateway, and GraphQL service implementations.

For any schema modifications, remember that the GraphQL gateway automatically composes schemas from federated services.

All development should follow the established gRPC patterns documented in the language-specific instruction files.

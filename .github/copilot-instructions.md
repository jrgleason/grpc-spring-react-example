# GitHub Copilot Instructions for gRPC-Web Full-Stack Project

## Project Overview
This is a full-stack gRPC-Web application with a React TypeScript frontend and Spring Boot Java backend. **All communication is via gRPC protocol only - no REST endpoints.**

## Architecture
- **Frontend**: React + TypeScript + gRPC-Web (Port 3000)
- **Proxy**: Envoy for gRPC-Web translation (Port 8080)  
- **Backend**: Spring Boot + gRPC Server (Port 9090)

## Language-Specific Instructions

### For Java Backend Development
See [java.md](./copilot-instructions/java.md) for comprehensive Spring Boot gRPC service implementation patterns, configuration, and best practices.

### For JavaScript/TypeScript Frontend Development  
See [javascript.md](./copilot-instructions/javascript.md) for React gRPC-Web client patterns, proto code generation, and TypeScript integration.

## General Guidelines

We use Protocol Buffers for all API definitions - never suggest REST endpoints or HTTP client libraries. Always work with the generated gRPC clients and services.

When suggesting architectural changes, consider the gRPC-Web proxy requirements and ensure compatibility between frontend gRPC-Web and backend gRPC implementations.

For any proto file modifications, remember that code regeneration is required for both frontend TypeScript clients and backend Java classes.

All development should follow the established gRPC patterns documented in the language-specific instruction files.

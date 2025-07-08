# CI/CD Pipeline

This project uses GitHub Actions for continuous integration and deployment.

## Workflow Overview

The CI/CD pipeline consists of several jobs that run on every push to `main`/`develop` branches and on pull requests:

### 1. Backend Testing (`test-backend`)
- Sets up JDK 24
- Runs Maven tests for the Spring Boot gRPC backend
- Builds the JAR artifact
- Caches Maven dependencies for faster builds

### 2. Node.js Services Testing (`test-nodejs-services`)
- Matrix strategy testing for all Node.js services:
  - `apollo-gateway` - Apollo Federation Gateway
  - `user-graphql-service` - GraphQL service wrapping gRPC
  - `frontend-graphql` - React frontend application
- Runs linting, testing, and building for each service
- Uses Node.js 18 with npm cache

### 3. Integration Testing (`integration-test`)
- Builds all Docker images using `docker-compose.graphql.yml`
- Starts the complete stack in Docker
- Performs health checks on all services:
  - gRPC Backend (port 9090)
  - User GraphQL Service (port 4001)
  - Apollo Gateway (port 4000)
  - Frontend (port 3000)
- Tests GraphQL endpoint with sample query
- Collects logs on failure

### 4. Security Scanning (`security-scan`)
- Runs Trivy vulnerability scanner on the codebase
- Uploads results to GitHub Security tab
- Only runs on push events (not PRs)

### 5. Build and Push (`build-and-push`)
- Only runs on main branch pushes after all tests pass
- Builds Docker images for all services
- Prepared for pushing to Docker Hub (commented out)
- Requires `DOCKER_USERNAME` and `DOCKER_PASSWORD` secrets

### 6. Deploy (`deploy`)
- Placeholder for deployment to staging environment
- Only runs on main branch after successful build
- Requires manual configuration based on deployment target

## Setup Requirements

### GitHub Secrets
To enable Docker Hub publishing, add these secrets to your repository:
- `DOCKER_USERNAME` - Your Docker Hub username
- `DOCKER_PASSWORD` - Your Docker Hub access token

### Environment Protection
The deploy job uses the `staging` environment. Configure this in your repository settings under Environments.

## Local Testing

You can test the integration locally using the same Docker Compose command:

```bash
# Build and start all services
docker-compose -f docker-compose.graphql.yml up --build

# Health checks
curl http://localhost:4001/health  # User service
curl http://localhost:4000/health  # Apollo Gateway  
curl http://localhost:3000         # Frontend

# Test GraphQL query
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"query":"{ users { id name email } }"}' \
  http://localhost:4000/graphql
```

## Customization

### Adding Real Tests
Currently, the Node.js services have placeholder test scripts. To add real tests:

1. Install testing framework (Jest, Mocha, etc.)
2. Add test files 
3. Update the `test` script in `package.json`
4. The workflow will automatically run them

### Deployment Configuration
Update the `deploy` job with your specific deployment commands:
- Kubernetes: `kubectl apply`
- AWS ECS: AWS CLI commands
- Docker Swarm: `docker stack deploy`
- SSH deployment: rsync and docker commands

### Matrix Testing
The Node.js services use a matrix strategy. Add new services by updating the matrix in `.github/workflows/ci-cd.yml`:

```yaml
strategy:
  matrix:
    service: [apollo-gateway, user-graphql-service, frontend-graphql, new-service]
```

## Monitoring

- View workflow runs in the "Actions" tab of your GitHub repository
- Security scan results appear in the "Security" tab
- Artifacts are available for download from successful runs

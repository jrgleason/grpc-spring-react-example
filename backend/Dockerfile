# Multi-stage build for Spring Boot gRPC Backend

# Build stage
FROM openjdk:24-jdk-slim AS builder

WORKDIR /app

# Copy Maven wrapper and pom.xml for dependency caching
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests -B

# Runtime stage
FROM openjdk:24-jdk-slim

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/grpc-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose gRPC port
EXPOSE 9090

# Run the application
CMD ["java", "-jar", "app.jar"]

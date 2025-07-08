# Multi-stage build for Spring Boot gRPC Backend

# Build stage
FROM maven:3.9-eclipse-temurin-24 AS builder

WORKDIR /app

# Copy pom.xml for dependency caching
COPY pom.xml ./

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/grpc-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose gRPC port
EXPOSE 9090

# Run the application
CMD ["java", "-jar", "app.jar"]

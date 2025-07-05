FROM openjdk:24-jdk-slim

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose gRPC port
EXPOSE 9090

# Run the application
CMD ["java", "-jar", "target/grpc-backend-0.0.1-SNAPSHOT.jar"]

FROM openjdk:24-jdk-slim

WORKDIR /app

# Copy the pre-built JAR file
COPY target/grpc-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose gRPC port
EXPOSE 9090

# Run the application
CMD ["java", "-jar", "app.jar"]

# Use OpenJDK image
#FROM openjdk:17-jdk-alpine
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven target directory
COPY target/transaction-service-0.0.1-SNAPSHOT.jar transaction-service.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "transaction-service.jar"]
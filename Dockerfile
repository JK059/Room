# Use the official maintained Eclipse Temurin image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file built by Maven in the previous Jenkins stage
# Ensure the filename matches your pom.xml (room-booking-1.0.0.jar)
COPY target/room-booking-1.0.0.jar app.jar

# Expose the application port
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]

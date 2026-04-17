# Use JDK 17 to run the app
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

COPY target/room-booking-1.0.0.jar app.jar

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

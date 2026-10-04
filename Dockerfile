# Use Eclipse Temurin Java 21 JRE Runtime image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy packaged Spring Boot jar
COPY target/enterprise-reporting-app-1.0.0.jar app.jar

# Expose HTTP port
EXPOSE 8080

# Run Spring Boot Application
ENTRYPOINT ["java", "-jar", "app.jar"]

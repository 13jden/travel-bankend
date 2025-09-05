# Multi-stage build for GitHub Actions
FROM eclipse-temurin:17-jdk-alpine AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
COPY travel-admin/pom.xml travel-admin/
RUN mvn dependency:go-offline -B

# Copy source code
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Set timezone
ENV TZ=Asia/Shanghai

# Install curl for health check
RUN apk add --no-cache curl

# Create upload directory
RUN mkdir -p /app/uploads

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Copy the built jar from builder stage
COPY --from=builder /app/travel-admin/target/travel-admin-0.0.1-SNAPSHOT.jar app.jar

# Verify the jar file
RUN jar tf app.jar | head -20

# Expose port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

# Start application
CMD ["java", "-jar", "app.jar"]

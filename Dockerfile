# syntax=docker/dockerfile:1

# ───────────────────────────────────────────────────────────────
# 1) Build stage: Maven + JDK 21 on Alpine
# ───────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# (1) Cache dependencies if only pom.xml changes
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# (2) Copy sources and package
COPY src ./src
RUN mvn clean package -DskipTests -B

# ───────────────────────────────────────────────────────────────
# 2) Runtime stage: slim OpenJDK 21
# ───────────────────────────────────────────────────────────────
FROM openjdk:21-jdk-slim AS runtime
WORKDIR /app

# Copy the fat JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

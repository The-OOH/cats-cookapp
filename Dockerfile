# syntax=docker/dockerfile:1

# ───────────────────────────────────────────────────────────────
# 1) Build stage: use the official Maven image with OpenJDK 21
# ───────────────────────────────────────────────────────────────
FROM maven:3.9.6-jdk-21-slim AS build
WORKDIR /app

# (1) copy just the POM and grab dependencies so they cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# (2) copy sources & compile
COPY src ./src
RUN mvn clean package -DskipTests -B

# ───────────────────────────────────────────────────────────────
# 2) Runtime stage: slim OpenJDK 21
# ───────────────────────────────────────────────────────────────
FROM openjdk:21-slim AS runtime
WORKDIR /app

# grab the JAR from the build image
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]

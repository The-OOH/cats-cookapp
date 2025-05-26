#
# Project build (Multi-Stage)
# --------------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS build

# (Optional) allow customizing which JAR to pick up
ARG JAR_NAME=target/microservice-0.0.1-SNAPSHOT.jar

COPY . .
RUN mvn clean package

# ────────────────────────────────────────────────────────────────────────────────
FROM openjdk:21

# Build-time args for wiring into runtime ENV
ARG DB_URL
ARG DB_USER
ARG DB_PASS
ARG CLERK_SECRET_KEY
ARG CLOUDFLARE_ACCESS_KEY
ARG CLOUDFLARE_SECRET_KEY
ARG CLOUDFLARE_ACCOUNT_ID
ARG CLOUDFLARE_BUCKET_NAME
ARG CLOUDFLARE_PUBLIC_URL

# Set runtime environment variables (with blank defaults)
ENV DB_URL=${DB_URL:-""} \
    DB_USER=${DB_USER:-""} \
    DB_PASS=${DB_PASS:-""} \
    CLERK_SECRET_KEY=${CLERK_SECRET_KEY:-""} \
    CLOUDFLARE_ACCESS_KEY=${CLOUDFLARE_ACCESS_KEY:-""} \
    CLOUDFLARE_SECRET_KEY=${CLOUDFLARE_SECRET_KEY:-""} \
    CLOUDFLARE_ACCOUNT_ID=${CLOUDFLARE_ACCOUNT_ID:-""} \
    CLOUDFLARE_BUCKET_NAME=${CLOUDFLARE_BUCKET_NAME:-""} \
    CLOUDFLARE_PUBLIC_URL=${CLOUDFLARE_PUBLIC_URL:-""}

EXPOSE 8080

# Copy the built JAR (using the ARG if you overrode it)
COPY --from=build /target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

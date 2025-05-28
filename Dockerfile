FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jdk-alpine AS runtime
WORKDIR /app

# Copy the fat JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV JVM_INIT_PERCENT=25.0
ENV JVM_MAX_PERCENT=50.0

ENTRYPOINT ["java",
  "-XX:+UseContainerSupport",
  "-XX:InitialRAMPercentage=25.0",
  "-XX:MaxRAMPercentage=50.0",
  "-jar", "app.jar"
]

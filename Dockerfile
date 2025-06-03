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

ENTRYPOINT ["java","-XX:+UseShenandoahGC","-Xmx768m","-Xms32m", "-XX:+UnlockExperimentalVMOptions", "-XX:ShenandoahUncommitDelay=1000", "-XX:ShenandoahGuaranteedGCInterval=10000", "-jar", "app.jar"]

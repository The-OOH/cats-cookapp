FROM openjdk:21
VOLUME /tmp
ARG JAR_FILE=*.jar
COPY target/${JAR_FILE} app.jar
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /app.jar \

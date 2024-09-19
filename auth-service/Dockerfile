FROM gradle:7.5-jdk17 AS build


RUN rm -rf /build/

WORKDIR /app

COPY gradle/wrapper/gradle-wrapper.properties gradle/wrapper/
COPY build.gradle settings.gradle ./
COPY src/ ./src/

RUN gradle bootJar

FROM openjdk:21-jdk-slim

ARG APP_NAME="auth-service"
ARG APP_VERSION="0.0.1"
ARG JAR_FILE="${APP_NAME}-${APP_VERSION}-SNAPSHOT.jar"

COPY --from=build /app/build/libs/${JAR_FILE} /app/app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

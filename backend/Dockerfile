FROM gradle:8.12.1-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/backend-*-SNAPSHOT.jar backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend.jar"]

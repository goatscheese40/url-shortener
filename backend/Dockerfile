FROM azul/zulu-openjdk-alpine:21
WORKDIR /app
COPY ./build/libs/backend-*-SNAPSHOT.jar ./backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend.jar"]

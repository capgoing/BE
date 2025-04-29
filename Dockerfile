FROM openjdk:17-alpine

WORKDIR /app

COPY build/libs/server-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app/app.jar"]


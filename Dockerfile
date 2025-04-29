FROM openjdk:17-jdk-slim as builder

WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar -x test

FROM openjdk:17-jre-slim

WORKDIR /app
COPY --from=builder /app/build/libs/server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

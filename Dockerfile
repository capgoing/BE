FROM openjdk:17-alpine

WORKDIR /app

# JAR 파일 복사
COPY build/libs/server-0.0.1-SNAPSHOT.jar app.jar

# JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]

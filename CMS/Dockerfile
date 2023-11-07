
FROM gradle:7.3.3-jdk17 AS build

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle/ ./gradle/
COPY src/ ./src/

RUN ./gradlew build -x test



FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*T.jar app.jar

EXPOSE 8090

CMD ["java", "-jar", "app.jar"]

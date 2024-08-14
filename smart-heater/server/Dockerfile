FROM maven:3.8.6 AS build
LABEL authors="tobias.schiphorst"

WORKDIR /backend

COPY pom.xml pom.xml
COPY src /backend/src
RUN mvn clean install -DskipTests

FROM openjdk:17-slim

COPY --from=build /backend/target/*.jar server.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server.jar"]
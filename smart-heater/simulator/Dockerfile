FROM maven:3.8.6 AS build
LABEL authors="tobias.schiphorst"

WORKDIR /simulator

COPY pom.xml pom.xml
COPY src /simulator/src
RUN mvn clean install -DskipTests

FROM openjdk:17-slim

COPY --from=build /simulator/target/*.jar heater.jar

EXPOSE 7777

ENTRYPOINT ["java", "-jar", "heater.jar"]
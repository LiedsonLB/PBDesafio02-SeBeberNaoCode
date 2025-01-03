FROM openjdk:17-jdk-slim as build

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY .. /app

RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/ms-authorization-0.0.1-SNAPSHOT.jar /app/ms-authorization.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "ms-authorization.jar"]

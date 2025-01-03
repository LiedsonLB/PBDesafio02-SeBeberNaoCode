FROM openjdk:17-jdk-slim as build

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY .. /app

RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/servereureka-0.0.1-SNAPSHOT.jar /app/servereureka.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "servereureka.jar"]

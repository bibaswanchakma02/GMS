# Use an official Maven image to build the application
# This image includes JDK 17 and Maven


#FROM maven:3.8.6-openjdk-17 AS build
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY ./target/gms-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

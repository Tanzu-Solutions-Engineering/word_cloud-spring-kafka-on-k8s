FROM openjdk:8-jdk-alpine
COPY ./target/cache-server-1.0.jar /app/
EXPOSE 40404
WORKDIR /app
ENTRYPOINT ["java","-jar","/app/cache-server-1.0.jar"]
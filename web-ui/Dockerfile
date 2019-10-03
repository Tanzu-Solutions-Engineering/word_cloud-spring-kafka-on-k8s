FROM openjdk:8-jdk-alpine
COPY ./target/web-ui-1.0.jar /app/
EXPOSE 8084
WORKDIR /app
ENTRYPOINT ["java","-jar","/app/web-ui-1.0.jar"]
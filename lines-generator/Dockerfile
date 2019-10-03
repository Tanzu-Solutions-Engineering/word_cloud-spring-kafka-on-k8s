FROM openjdk:8-jdk-alpine
COPY ./target/lines-generator-1.0.jar /app/
EXPOSE 8080
WORKDIR /app
ENTRYPOINT ["java","-jar","/app/lines-generator-1.0.jar"]
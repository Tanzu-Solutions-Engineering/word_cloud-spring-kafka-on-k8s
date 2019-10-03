FROM openjdk:8
COPY ./target/windowed-wordcount-stream-1.0.jar /app/
EXPOSE 8082
WORKDIR /app
ENTRYPOINT ["java","-jar","/app/windowed-wordcount-stream-1.0.jar"]
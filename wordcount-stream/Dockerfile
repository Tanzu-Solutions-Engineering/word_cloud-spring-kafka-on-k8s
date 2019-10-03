FROM openjdk:8
COPY ./target/wordcount-stream-1.0.jar /app/
EXPOSE 8083
WORKDIR /app
ENTRYPOINT ["java","-jar","/app/wordcount-stream-1.0.jar"]
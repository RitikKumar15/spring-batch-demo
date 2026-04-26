FROM openjdk:17-jdk-alpine
EXPOSE 8080
COPY target/spring-batch-demo.jar spring-batch-demo.jar
ENTRYPOINT ["java","-jar","spring-batch-demo.jar"]
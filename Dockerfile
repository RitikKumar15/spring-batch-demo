FROM openjdk:17-jdk-alpine
EXPOSE 9090
COPY target/spring-batch-demo.jar spring-batch-demo.jar
ENTRYPOINT ["java","-jar","spring-batch-demo.jar"]
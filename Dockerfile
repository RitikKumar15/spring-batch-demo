FROM amazoncorretto/17-alpine-jdk
EXPOSE 8080
COPY target/spring-batch-demo.jar spring-batch-demo.jar
ENTRYPOINT ["java","-jar","spring-batch-demo.jar"]

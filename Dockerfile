FROM openjdk:17-jdk
COPY target/trace-origin.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","trace-origin.jar"]x
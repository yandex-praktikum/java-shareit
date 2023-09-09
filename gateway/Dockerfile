FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar client.jar
ENTRYPOINT ["java","-jar","/client.jar"]